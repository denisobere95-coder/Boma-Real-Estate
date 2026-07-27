package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.api.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BomaUiState(
    val selectedCategory: String = "All", // "All", "Buy", "Rent", "Land", "Commercial"
    val searchQuery: String = "",
    val locationFilter: String = "All",
    val propertyTypeFilter: String = "All",
    val maxPriceKsh: Long = 100_000_000,
    val selectedProperty: Property? = null,
    val activeTab: Int = 0, // 0: Explore, 1: Saved, 2: Anti-Fraud & Verification, 3: Vault & Mortgage, 4: Agent Portal
    val isScheduleViewingOpen: Boolean = false,
    val isFraudReportOpen: Boolean = false,
    val is360TourOpen: Boolean = false,
    val isAddListingOpen: Boolean = false,
    val isAddDocumentOpen: Boolean = false,
    val isBiometricEnabled: Boolean = true,
    val isVaultUnlocked: Boolean = false,
    val biometricVerifiedAt: Long? = null,
    val liveMarketMetadata: IntegratedMarketMetadata? = null,
    val apiIntegrationStatus: ApiIntegrationStatus? = null,
    val isFetchingLiveApiData: Boolean = false,
    val isSearchFocused: Boolean = false,
    val recentSearches: List<RecentSearch> = emptyList(),
    val locationSuggestions: List<String> = emptyList(),
    val snackbarMessage: String? = null
)

class BomaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BomaRepository
    private val liveMarketRepository = LiveMarketDataRepository()

    private val _uiState = MutableStateFlow(BomaUiState())
    val uiState: StateFlow<BomaUiState> = _uiState.asStateFlow()

    init {
        val database = BomaDatabase.getDatabase(application)
        repository = BomaRepository(
            database.bomaDao(),
            database.favoritePropertyDao(),
            database.recentSearchDao()
        )
        loadApiStatus()

        viewModelScope.launch {
            repository.recentSearches.collect { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }
        _uiState.update {
            it.copy(locationSuggestions = repository.getPopularKenyaLocations(""))
        }
    }

    val properties: StateFlow<List<Property>> = repository.allProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedProperties: StateFlow<List<Property>> = repository.savedProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val viewingRequests: StateFlow<List<ViewingRequest>> = repository.viewingRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documentItems: StateFlow<List<DocumentVaultItem>> = repository.documents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mortgageLeads: StateFlow<List<MortgageLead>> = repository.mortgageLeads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProperties: StateFlow<List<Property>> = combine(
        properties,
        uiState
    ) { allProps, state ->
        allProps.filter { prop ->
            val matchesCategory = when {
                state.selectedCategory == "All" -> true
                state.selectedCategory.equals("Residential", ignoreCase = true) -> {
                    prop.category.equals("Residential", ignoreCase = true) ||
                    prop.category.equals("Buy", ignoreCase = true) ||
                    prop.category.equals("Rent", ignoreCase = true) ||
                    prop.propertyType.contains("Apartment", ignoreCase = true) ||
                    prop.propertyType.contains("Villa", ignoreCase = true) ||
                    prop.propertyType.contains("House", ignoreCase = true)
                }
                state.selectedCategory.equals("Commercial", ignoreCase = true) -> {
                    prop.category.equals("Commercial", ignoreCase = true) ||
                    prop.propertyType.contains("Office", ignoreCase = true) ||
                    prop.propertyType.contains("Retail", ignoreCase = true)
                }
                state.selectedCategory.equals("Land", ignoreCase = true) -> {
                    prop.category.equals("Land", ignoreCase = true) ||
                    prop.propertyType.contains("Plot", ignoreCase = true) ||
                    prop.propertyType.contains("Land", ignoreCase = true)
                }
                else -> prop.category.equals(state.selectedCategory, ignoreCase = true)
            }
            val matchesSearch = state.searchQuery.isBlank() ||
                    prop.title.contains(state.searchQuery, ignoreCase = true) ||
                    prop.location.contains(state.searchQuery, ignoreCase = true) ||
                    prop.propertyType.contains(state.searchQuery, ignoreCase = true)
            val matchesLocation = (state.locationFilter == "All" || prop.location.contains(state.locationFilter, ignoreCase = true))
            val matchesPrice = prop.priceKsh <= state.maxPriceKsh

            matchesCategory && matchesSearch && matchesLocation && matchesPrice
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(activeTab = tabIndex) }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        val suggestions = repository.getPopularKenyaLocations(query)
        _uiState.update {
            it.copy(
                searchQuery = query,
                locationSuggestions = suggestions
            )
        }
    }

    fun submitSearch(query: String = uiState.value.searchQuery) {
        if (query.isNotBlank()) {
            _uiState.update { it.copy(searchQuery = query) }
            viewModelScope.launch {
                repository.saveRecentSearch(query)
            }
        }
    }

    fun setSearchFocused(focused: Boolean) {
        _uiState.update { it.copy(isSearchFocused = focused) }
    }

    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            repository.deleteRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            repository.clearRecentSearches()
        }
    }

    fun setLocationFilter(loc: String) {
        _uiState.update { it.copy(locationFilter = loc) }
    }

    fun loadApiStatus() {
        val status = liveMarketRepository.getApiStatus()
        _uiState.update { it.copy(apiIntegrationStatus = status) }
    }

    fun fetchLiveMarketData(locationName: String, bedrooms: Int = 3, areaSqFt: Int = 1800) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingLiveApiData = true) }
            val metadata = liveMarketRepository.fetchMarketMetadataForLocation(locationName, bedrooms, areaSqFt)
            _uiState.update {
                it.copy(
                    liveMarketMetadata = metadata,
                    isFetchingLiveApiData = false
                )
            }
        }
    }

    fun selectProperty(property: Property?) {
        _uiState.update { it.copy(selectedProperty = property) }
        property?.let { prop ->
            fetchLiveMarketData(prop.location, prop.bedrooms, prop.areaSqFt)
        }
    }


    fun toggleSaveProperty(property: Property) {
        viewModelScope.launch {
            val newSaveState = !property.isSaved
            repository.setSaved(property.id, newSaveState)
            showSnackbar(if (newSaveState) "Property saved to your favorites!" else "Property removed from favorites.")
            // Also update selected property state if open
            if (_uiState.value.selectedProperty?.id == property.id) {
                _uiState.update { currentState ->
                    currentState.copy(selectedProperty = property.copy(isSaved = newSaveState))
                }
            }
        }
    }

    fun openScheduleViewing() {
        _uiState.update { it.copy(isScheduleViewingOpen = true) }
    }

    fun closeScheduleViewing() {
        _uiState.update { it.copy(isScheduleViewingOpen = false) }
    }

    fun openFraudReportModal() {
        _uiState.update { it.copy(isFraudReportOpen = true) }
    }

    fun closeFraudReportModal() {
        _uiState.update { it.copy(isFraudReportOpen = false) }
    }

    fun open360TourModal() {
        _uiState.update { it.copy(is360TourOpen = true) }
    }

    fun close360TourModal() {
        _uiState.update { it.copy(is360TourOpen = false) }
    }

    fun openAddListingModal() {
        _uiState.update { it.copy(isAddListingOpen = true) }
    }

    fun closeAddListingModal() {
        _uiState.update { it.copy(isAddListingOpen = false) }
    }

    fun openAddDocumentModal() {
        _uiState.update { it.copy(isAddDocumentOpen = true) }
    }

    fun closeAddDocumentModal() {
        _uiState.update { it.copy(isAddDocumentOpen = false) }
    }

    fun submitViewingRequest(
        date: String,
        timeSlot: String,
        clientName: String,
        clientPhone: String
    ) {
        val selectedProp = _uiState.value.selectedProperty ?: return
        viewModelScope.launch {
            val req = ViewingRequest(
                propertyId = selectedProp.id,
                propertyTitle = selectedProp.title,
                agentName = selectedProp.agentName,
                date = date,
                timeSlot = timeSlot,
                clientName = clientName,
                clientPhone = clientPhone
            )
            repository.addViewingRequest(req)
            closeScheduleViewing()
            showSnackbar("Viewing request scheduled with ${selectedProp.agentName} for $date ($timeSlot)!")
        }
    }

    fun submitFraudReport(reason: String, details: String) {
        val selectedProp = _uiState.value.selectedProperty ?: return
        viewModelScope.launch {
            repository.reportFraud(selectedProp.id)
            closeFraudReportModal()
            showSnackbar("Fraud report logged! Our compliance team is verifying EARB license & title records.")
        }
    }

    fun submitMortgageLead(
        propertyTitle: String,
        priceKsh: Long,
        depositKsh: Long,
        loanTermYears: Int,
        interestRatePercent: Double,
        monthlyPaymentKsh: Long,
        bankName: String,
        applicantName: String,
        phone: String,
        email: String
    ) {
        viewModelScope.launch {
            val lead = MortgageLead(
                propertyTitle = propertyTitle,
                propertyPriceKsh = priceKsh,
                depositAmountKsh = depositKsh,
                loanTermYears = loanTermYears,
                interestRatePercent = interestRatePercent,
                estimatedMonthlyPaymentKsh = monthlyPaymentKsh,
                preferredBank = bankName,
                applicantName = applicantName,
                applicantPhone = phone,
                applicantEmail = email
            )
            repository.addMortgageLead(lead)
            showSnackbar("Mortgage pre-qualification lead sent to $bankName! An advisor will call $phone.")
        }
    }

    fun createNewPropertyListing(
        title: String,
        category: String,
        propertyType: String,
        priceKsh: Long,
        pricePeriod: String,
        location: String,
        bedrooms: Int,
        bathrooms: Int,
        areaSqFt: Int,
        description: String,
        agentName: String,
        agentPhone: String
    ) {
        viewModelScope.launch {
            val newProp = Property(
                id = "prop_" + System.currentTimeMillis().toString().takeLast(6),
                title = title,
                category = category,
                propertyType = propertyType,
                priceKsh = priceKsh,
                pricePeriod = if (pricePeriod.isBlank()) "Total Price" else pricePeriod,
                location = location,
                bedrooms = bedrooms,
                bathrooms = bathrooms,
                areaSqFt = areaSqFt,
                description = description,
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/NEW",
                agentName = agentName.ifBlank { "You (Agent)" },
                agentPhone = agentPhone.ifBlank { "+254 700 000 000" },
                agentRating = 5.0,
                imageDrawableName = "img_nairobi_apartment_1785090831216",
                imageUrl = when {
                    propertyType.contains("Plot", ignoreCase = true) || propertyType.contains("Land", ignoreCase = true) ->
                        "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=80"
                    propertyType.contains("Villa", ignoreCase = true) || propertyType.contains("House", ignoreCase = true) ->
                        "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1200&q=80"
                    propertyType.contains("Office", ignoreCase = true) || propertyType.contains("Retail", ignoreCase = true) ->
                        "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80"
                    else -> "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=1200&q=80"
                },
                is360TourAvailable = true
            )
            repository.addProperty(newProp)
            closeAddListingModal()
            showSnackbar("New property listing published successfully!")
        }
    }

    fun createDocumentVaultItem(
        docType: String,
        docName: String,
        propertyTitle: String
    ) {
        viewModelScope.launch {
            val doc = DocumentVaultItem(
                docType = docType,
                docName = docName,
                propertyTitle = propertyTitle,
                dateAdded = "2026-07-26",
                fileSize = "2.4 MB",
                status = "ArdhiHouse Verified",
                verificationHash = "0x" + System.currentTimeMillis().toString(16).uppercase().take(8)
            )
            repository.addDocument(doc)
            closeAddDocumentModal()
            showSnackbar("Document '$docName' securely uploaded & verified in Vault!")
        }
    }

    fun setBiometricUnlocked(unlocked: Boolean, customMsg: String? = null) {
        _uiState.update {
            it.copy(
                isVaultUnlocked = unlocked,
                biometricVerifiedAt = if (unlocked) System.currentTimeMillis() else null,
                snackbarMessage = customMsg ?: if (unlocked) "Biometric verification successful! Title Deed Vault Unlocked." else "Title Deed Vault Locked."
            )
        }
    }

    fun toggleBiometricEnabled(enabled: Boolean) {
        _uiState.update {
            it.copy(
                isBiometricEnabled = enabled,
                isVaultUnlocked = if (!enabled) true else false,
                snackbarMessage = if (enabled) "Biometric Lock enabled." else "Biometric Lock disabled."
            )
        }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
