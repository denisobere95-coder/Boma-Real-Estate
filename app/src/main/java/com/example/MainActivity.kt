package com.example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.BomaViewModel
import com.example.ui.components.*
import com.example.ui.theme.BomaTheme
import com.example.ui.theme.EmeraldPrimary

class MainActivity : FragmentActivity() {

    private val viewModel: BomaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BomaTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val filteredProperties by viewModel.filteredProperties.collectAsStateWithLifecycle()
                val savedProperties by viewModel.savedProperties.collectAsStateWithLifecycle()
                val viewingRequests by viewModel.viewingRequests.collectAsStateWithLifecycle()
                val documentItems by viewModel.documentItems.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        if (uiState.selectedProperty == null && !uiState.is360TourOpen) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                windowInsets = WindowInsets.navigationBars,
                                tonalElevation = 3.dp
                            ) {
                                val navColors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldPrimary,
                                    selectedTextColor = EmeraldPrimary,
                                    indicatorColor = Color(0xFFEFF6FF),
                                    unselectedIconColor = Color(0xFF64748B),
                                    unselectedTextColor = Color(0xFF64748B)
                                )
                                NavigationBarItem(
                                    selected = uiState.activeTab == 0,
                                    onClick = { viewModel.selectTab(0) },
                                    icon = { Icon(Icons.Default.HomeWork, contentDescription = "Explore") },
                                    label = { Text("Explore", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                                    colors = navColors
                                )
                                NavigationBarItem(
                                    selected = uiState.activeTab == 1,
                                    onClick = { viewModel.selectTab(1) },
                                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                                    label = { Text("Saved", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                                    colors = navColors
                                )
                                NavigationBarItem(
                                    selected = uiState.activeTab == 2,
                                    onClick = { viewModel.selectTab(2) },
                                    icon = { Icon(Icons.Default.Shield, contentDescription = "Anti-Fraud") },
                                    label = { Text("Anti-Fraud", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                                    colors = navColors
                                )
                                NavigationBarItem(
                                    selected = uiState.activeTab == 3,
                                    onClick = { viewModel.selectTab(3) },
                                    icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "Vault") },
                                    label = { Text("Vault", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                                    colors = navColors
                                )
                                NavigationBarItem(
                                    selected = uiState.activeTab == 4,
                                    onClick = { viewModel.selectTab(4) },
                                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Agent Portal") },
                                    label = { Text("Agent Portal", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) },
                                    colors = navColors
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Main Navigation Screen Tabs
                        when (uiState.activeTab) {
                            0 -> ExploreScreen(
                                uiState = uiState,
                                properties = filteredProperties,
                                onCategorySelect = viewModel::selectCategory,
                                onSearchQueryChange = viewModel::updateSearchQuery,
                                onSearchSubmit = viewModel::submitSearch,
                                onDeleteRecentSearch = viewModel::deleteRecentSearch,
                                onClearRecentSearches = viewModel::clearRecentSearches,
                                onLocationFilterSelect = viewModel::setLocationFilter,
                                onPropertyClick = viewModel::selectProperty,
                                onSaveToggle = viewModel::toggleSaveProperty,
                                onOpenAddListing = viewModel::openAddListingModal
                            )
                            1 -> SavedScreen(
                                savedProperties = savedProperties,
                                onPropertyClick = viewModel::selectProperty,
                                onSaveToggle = viewModel::toggleSaveProperty
                            )
                            2 -> FraudPreventionScreen(
                                onReportFraudClick = {
                                    if (uiState.selectedProperty == null && filteredProperties.isNotEmpty()) {
                                        viewModel.selectProperty(filteredProperties.first())
                                    }
                                    viewModel.openFraudReportModal()
                                },
                                onRunBiometricAudit = {
                                    com.example.util.BiometricAuthHelper.promptBiometricAuth(
                                        activity = this@MainActivity,
                                        title = "ArdhiSasa Land Registry Audit",
                                        subtitle = "Biometric Fraud Prevention Scan",
                                        description = "Scan your fingerprint or face to authenticate national land registry record audit.",
                                        onSuccess = {
                                            viewModel.showSnackbar("Biometric Verification Passed! Title Deed is 100% Authentic & Cleared.")
                                        },
                                        onError = { error ->
                                            viewModel.showSnackbar("Biometric Scan: $error")
                                        }
                                    )
                                }
                            )
                            3 -> DocumentVaultScreen(
                                documents = documentItems,
                                isVaultUnlocked = uiState.isVaultUnlocked,
                                onAuthenticateBiometric = {
                                    com.example.util.BiometricAuthHelper.promptBiometricAuth(
                                        activity = this@MainActivity,
                                        title = "Boma Vault Security Authentication",
                                        subtitle = "Encrypted Title Deed Access",
                                        description = "Scan fingerprint or face ID to unlock sensitive land titles & contracts.",
                                        onSuccess = {
                                            viewModel.setBiometricUnlocked(true, "Biometric authentication verified! Title Deed Vault Unlocked.")
                                        },
                                        onError = { error ->
                                            viewModel.showSnackbar("Biometric Auth: $error")
                                        }
                                    )
                                },
                                onLockVault = {
                                    viewModel.setBiometricUnlocked(false, "Title Deed Vault Locked.")
                                },
                                onAddDocumentClick = viewModel::openAddDocumentModal,
                                onSubmitMortgageLead = viewModel::submitMortgageLead
                            )
                            4 -> AgentDashboardScreen(
                                properties = filteredProperties,
                                viewings = viewingRequests,
                                onOpenAddListing = viewModel::openAddListingModal
                            )
                        }

                        // Full Screen Property Detail View Overlay
                        AnimatedVisibility(
                            visible = uiState.selectedProperty != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            uiState.selectedProperty?.let { selectedProp ->
                                PropertyDetailScreen(
                                    property = selectedProp,
                                    liveMarketMetadata = uiState.liveMarketMetadata,
                                    apiStatus = uiState.apiIntegrationStatus,
                                    isFetchingLiveApiData = uiState.isFetchingLiveApiData,
                                    onRefreshLiveApiData = {
                                        viewModel.fetchLiveMarketData(selectedProp.location, selectedProp.bedrooms, selectedProp.areaSqFt)
                                    },
                                    onBack = { viewModel.selectProperty(null) },
                                    onSaveToggle = viewModel::toggleSaveProperty,
                                    onOpenScheduleViewing = viewModel::openScheduleViewing,
                                    onOpen360Tour = viewModel::open360TourModal,
                                    onOpenFraudReport = viewModel::openFraudReportModal,
                                    onOpenMortgageCalculator = {
                                        viewModel.selectProperty(null)
                                        viewModel.selectTab(3)
                                    }
                                )
                            }
                        }


                        // 360° Virtual Tour Full-Screen Viewer Overlay
                        AnimatedVisibility(
                            visible = uiState.is360TourOpen,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            uiState.selectedProperty?.let { selectedProp ->
                                VirtualTourViewer(
                                    property = selectedProp,
                                    onClose = viewModel::close360TourModal
                                )
                            }
                        }

                        // Dialog Modals
                        if (uiState.isScheduleViewingOpen && uiState.selectedProperty != null) {
                            ScheduleViewingModal(
                                property = uiState.selectedProperty!!,
                                onDismiss = viewModel::closeScheduleViewing,
                                onSubmit = viewModel::submitViewingRequest
                            )
                        }

                        if (uiState.isFraudReportOpen && uiState.selectedProperty != null) {
                            FraudReportModal(
                                property = uiState.selectedProperty!!,
                                onDismiss = viewModel::closeFraudReportModal,
                                onSubmit = viewModel::submitFraudReport
                            )
                        }

                        if (uiState.isAddListingOpen) {
                            AddListingModal(
                                onDismiss = viewModel::closeAddListingModal,
                                onSubmit = viewModel::createNewPropertyListing
                            )
                        }

                        if (uiState.isAddDocumentOpen) {
                            AddDocumentModal(
                                onDismiss = viewModel::closeAddDocumentModal,
                                onSubmit = viewModel::createDocumentVaultItem
                            )
                        }
                    }
                }
            }
        }
    }
}
