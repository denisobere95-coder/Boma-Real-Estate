package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

class BomaRepository(
    private val dao: BomaDao,
    private val favoriteDao: FavoritePropertyDao? = null,
    private val recentSearchDao: RecentSearchDao? = null
) {

    val allProperties: Flow<List<Property>> = dao.getAllProperties()
        .onStart {
            ensureInitialDataSeeded()
        }

    val savedProperties: Flow<List<Property>> = dao.getSavedProperties()
    val favoritePropertyIds: Flow<List<String>>? = favoriteDao?.getAllFavoritePropertyIds()
    val recentSearches: Flow<List<RecentSearch>> = recentSearchDao?.getRecentSearches() ?: flowOf(emptyList())
    val viewingRequests: Flow<List<ViewingRequest>> = dao.getAllViewingRequests()

    suspend fun saveRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            recentSearchDao?.insertSearch(RecentSearch(query = trimmed))
        }
    }

    suspend fun deleteRecentSearch(query: String) {
        recentSearchDao?.deleteSearch(query)
    }

    suspend fun clearRecentSearches() {
        recentSearchDao?.clearAll()
    }

    fun getPopularKenyaLocations(query: String = ""): List<String> {
        val popularLocations = listOf(
            "Kilimani, Nairobi",
            "Karen, Nairobi",
            "Westlands, Nairobi",
            "Lavington, Nairobi",
            "Riverside, Nairobi",
            "Kileleshwa, Nairobi",
            "Parklands, Nairobi",
            "Gigiri, Nairobi",
            "Runda, Nairobi",
            "Muthaiga, Nairobi",
            "Ruaka, Kiambu",
            "Thika, Kiambu",
            "Naivasha, Nakuru",
            "Nakuru Town",
            "Malindi, Kilifi",
            "Nyali, Mombasa",
            "Diani, Kwale",
            "Eldoret, Uasin Gishu",
            "Machakos Town",
            "Kitengela, Kajiado"
        )
        if (query.isBlank()) return popularLocations.take(8)
        val trimmed = query.trim()
        return popularLocations.filter {
            it.contains(trimmed, ignoreCase = true)
        }.ifEmpty {
            // Also match against individual words in location
            popularLocations.filter { loc ->
                trimmed.split(" ").any { word -> loc.contains(word, ignoreCase = true) }
            }
        }
    }
    val documents: Flow<List<DocumentVaultItem>> = dao.getAllDocuments()
    val mortgageLeads: Flow<List<MortgageLead>> = dao.getAllMortgageLeads()

    suspend fun setSaved(id: String, isSaved: Boolean) {
        dao.setSavedStatus(id, isSaved)
        favoriteDao?.let { fDao ->
            if (isSaved) {
                fDao.addFavorite(FavoriteProperty(propertyId = id))
            } else {
                fDao.removeFavorite(id)
            }
        }
    }

    suspend fun reportFraud(id: String) {
        dao.incrementFraudReport(id)
    }

    suspend fun addViewingRequest(request: ViewingRequest) {
        dao.insertViewingRequest(request)
    }

    suspend fun addDocument(document: DocumentVaultItem) {
        dao.insertDocument(document)
    }

    suspend fun addMortgageLead(lead: MortgageLead) {
        dao.insertMortgageLead(lead)
    }

    suspend fun addProperty(property: Property) {
        dao.insertProperty(property)
    }

    private suspend fun ensureInitialDataSeeded() {
        val sampleList = listOf(
            Property(
                id = "prop_001",
                title = "3 BR Luxury Kilimani Horizon Apartment",
                category = "Residential",
                propertyType = "Apartment",
                priceKsh = 18_500_000,
                pricePeriod = "Total Price",
                location = "Kilimani, Nairobi",
                bedrooms = 3,
                bathrooms = 3,
                areaSqFt = 1850,
                description = "Modern high-floor 3-bedroom apartment with panoramic views of Nairobi skyline. Features en-suite master bedroom, solar water heater, fitted Italian kitchen, CCTV surveillance, backup generator, high speed lifts, and M-Pesa verified escrow booking.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/0481",
                agentName = "David Mutua",
                agentPhone = "+254 712 345 678",
                agentRating = 4.9,
                imageDrawableName = "img_nairobi_apartment_1785090831216",
                imageUrl = "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = true,
                isSaved = true
            ),
            Property(
                id = "prop_002",
                title = "5 BR Mansionette on 0.5 Acre",
                category = "Residential",
                propertyType = "Villa / House",
                priceKsh = 85_000_000,
                pricePeriod = "Total Price",
                location = "Karen, Nairobi",
                bedrooms = 5,
                bathrooms = 6,
                areaSqFt = 5200,
                description = "Stunning colonial-style villa in Karen on a mature manicured half-acre lawn. Features private heated pool, detached 2-bedroom DSQ, electric perimeter fence, borehole water, and ArdhiHouse title deed verification complete.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/0112",
                agentName = "Wanjiku Njeri",
                agentPhone = "+254 722 987 654",
                agentRating = 5.0,
                imageDrawableName = "img_karen_villa_1785090845590",
                imageUrl = "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = true,
                isSaved = false
            ),
            Property(
                id = "prop_003",
                title = "Prime Commercial Office Space - Apex Tower",
                category = "Commercial",
                propertyType = "Office / Retail",
                priceKsh = 350_000,
                pricePeriod = "/ month",
                location = "Westlands, Nairobi",
                bedrooms = 0,
                bathrooms = 4,
                areaSqFt = 2800,
                description = "Grade-A office suite in Westlands commercial hub. Features open-plan flexible layouts, high-speed fiber connectivity, central AC, 4 dedicated basement parking slots, and 24/7 security control room.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/C/2025/0883",
                agentName = "Otieno James",
                agentPhone = "+254 733 112 233",
                agentRating = 4.8,
                imageDrawableName = "img_westlands_office_1785090860061",
                imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = true,
                isSaved = false
            ),
            Property(
                id = "prop_004",
                title = "Naivasha Lakeview Gated Plots (1/8th Acre)",
                category = "Land",
                propertyType = "Plot / Land",
                priceKsh = 2_400_000,
                pricePeriod = "Total Price",
                location = "Naivasha, Nakuru",
                bedrooms = 0,
                bathrooms = 0,
                areaSqFt = 5400,
                description = "Prime residential plot in a gated controlled development overlooking Lake Naivasha. Water and electricity on site, ready beaconed with individual title deed, zero encumbrances, and instant digital land registry verification certificate.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/0921",
                agentName = "Peter Kamau",
                agentPhone = "+254 701 554 433",
                agentRating = 4.7,
                imageDrawableName = "img_boma_logo_1785090817566",
                imageUrl = "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = false,
                isSaved = false
            ),
            Property(
                id = "prop_005",
                title = "4 BR Executive Penthouse Riverside Drive",
                category = "Residential",
                propertyType = "Apartment",
                priceKsh = 180_000,
                pricePeriod = "/ month",
                location = "Riverside, Nairobi",
                bedrooms = 4,
                bathrooms = 4,
                areaSqFt = 3100,
                description = "Expansive penthouse apartment in Riverside Drive. Features rooftop terrace, infinity swimming pool, sauna, fully equipped gym, smart access control, and 24-hr security patrol.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/0331",
                agentName = "Amina Hassan",
                agentPhone = "+254 720 889 900",
                agentRating = 4.9,
                imageDrawableName = "img_karen_villa_1785090845590",
                imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = true,
                isSaved = false
            ),
            Property(
                id = "prop_006",
                title = "5-Acre Prime Agricultural Land Naivasha Rift Valley",
                category = "Land",
                propertyType = "Plot / Land",
                priceKsh = 14_500_000,
                pricePeriod = "Total Price",
                location = "Naivasha, Nakuru",
                bedrooms = 0,
                bathrooms = 0,
                areaSqFt = 217800,
                description = "Fertile 5-acre agricultural parcel suitable for greenhouse farming, flower production, or eco-resort development. Features tarmac access, red soil, three-phase power grid nearby, and Freehold Title Deed fully verified on ArdhiSasa.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/L/2026/0109",
                agentName = "Joseph Ochieng",
                agentPhone = "+254 711 223 344",
                agentRating = 4.9,
                imageDrawableName = "img_boma_logo_1785090817566",
                imageUrl = "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = false,
                isSaved = false
            ),
            Property(
                id = "prop_007",
                title = "Malindi Oceanfront Beach Plot (0.5 Acre)",
                category = "Land",
                propertyType = "Plot / Land",
                priceKsh = 9_800_000,
                pricePeriod = "Total Price",
                location = "Malindi, Kilifi",
                bedrooms = 0,
                bathrooms = 0,
                areaSqFt = 21780,
                description = "Exclusive half-acre beachfront land in Casuarina, Malindi. Ideal for a luxury holiday villa or boutique hotel. NEMA environmental clearance approval complete, clear beacon boundary markers, and clean title deed.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/K/2026/0552",
                agentName = "Fatuma Said",
                agentPhone = "+254 705 998 877",
                agentRating = 5.0,
                imageDrawableName = "img_boma_logo_1785090817566",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = false,
                isSaved = false
            ),
            Property(
                id = "prop_008",
                title = "4 BR Modern Townhouse Lavington",
                category = "Residential",
                propertyType = "Villa / House",
                priceKsh = 250_000,
                pricePeriod = "/ month",
                location = "Lavington, Nairobi",
                bedrooms = 4,
                bathrooms = 5,
                areaSqFt = 3800,
                description = "Charming 4-bedroom all en-suite townhouse in a quiet gated court of 6 units in Lavington. Solar heating, private garden, DSQ, perimeter electric fence with 24/7 guarded security.",
                isAgentVerified = true,
                isTitleDeedVerified = true,
                earbLicenseNo = "EARB/A/2026/0774",
                agentName = "Grace Wambui",
                agentPhone = "+254 722 334 455",
                agentRating = 4.8,
                imageDrawableName = "img_karen_villa_1785090845590",
                imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
                is360TourAvailable = true,
                isSaved = false
            )
        )

        dao.insertProperties(sampleList)
        favoriteDao?.let { fDao ->
            sampleList.filter { it.isSaved }.forEach { prop ->
                fDao.addFavorite(FavoriteProperty(propertyId = prop.id))
            }
        }

        // Seed initial documents if empty
        val sampleDocs = listOf(
            DocumentVaultItem(
                docType = "Title Deed",
                docName = "Kilimani_IR_98421_TitleDeed.pdf",
                propertyTitle = "3 BR Luxury Kilimani Horizon Apartment",
                dateAdded = "2026-07-15",
                fileSize = "3.2 MB",
                status = "ArdhiHouse Verified",
                verificationHash = "0x8F92A14E"
            ),
            DocumentVaultItem(
                docType = "Land Search Certificate",
                docName = "LandSearch_Naivasha_Plot402.pdf",
                propertyTitle = "Naivasha Lakeview Gated Plots",
                dateAdded = "2026-07-20",
                fileSize = "1.8 MB",
                status = "Ministry Verified",
                verificationHash = "0x3C19B880"
            )
        )
        for (doc in sampleDocs) {
            dao.insertDocument(doc)
        }
    }
}
