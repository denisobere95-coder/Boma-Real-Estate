package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import com.example.data.Property
import com.example.ui.BomaUiState
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SoftBlueHighlight
import com.example.ui.theme.TerracottaGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    uiState: BomaUiState,
    properties: List<Property>,
    viewingCount: Int = 3,
    onCategorySelect: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit = {},
    onDeleteRecentSearch: (String) -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    onLocationFilterSelect: (String) -> Unit,
    onPropertyClick: (Property) -> Unit,
    onSaveToggle: (Property) -> Unit,
    onOpenAddListing: () -> Unit
) {
    var isMapViewMode by remember { mutableStateOf(false) }
    var isSearchFocused by remember { mutableStateOf(false) }

    val categories = listOf("All", "Residential", "Commercial", "Land")
    val locations = listOf("All", "Kilimani", "Karen", "Westlands", "Naivasha", "Malindi", "Lavington", "Riverside")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header - Nyumbani Pro / Boma Real Estate
        Surface(
            tonalElevation = 1.dp,
            shadowElevation = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NYUMBANI PRO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Real Estate Kenya",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Built by Denis Obere Alumasi",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = isMapViewMode,
                            onClick = { isMapViewMode = !isMapViewMode },
                            label = {
                                Text(
                                    if (isMapViewMode) "List" else "Map",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (isMapViewMode) Icons.Default.List else Icons.Default.Map,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )

                        // User Initials Badge
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = SoftBlueHighlight,
                            border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "DO",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = EmeraldPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { newQuery ->
                        onSearchQueryChange(newQuery)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isSearchFocused = focusState.isFocused
                        },
                    placeholder = {
                        Text(
                            "Search Kilimani, Westlands, Karen...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8))
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearchSubmit(uiState.searchQuery)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                // Recent Searches Chip Group
                if (uiState.recentSearches.isNotEmpty() && (isSearchFocused || uiState.searchQuery.isNotBlank())) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Recent Searches",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF334155)
                                )
                            )
                        }
                        TextButton(
                            onClick = onClearRecentSearches,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Clear All",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.recentSearches, key = { it.query }) { recentSearch ->
                            InputChip(
                                selected = false,
                                onClick = {
                                    onSearchQueryChange(recentSearch.query)
                                    onSearchSubmit(recentSearch.query)
                                },
                                label = {
                                    Text(
                                        recentSearch.query,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color(0xFF64748B)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove search",
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable { onDeleteRecentSearch(recentSearch.query) },
                                        tint = Color(0xFF94A3B8)
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = Color(0xFFF1F5F9),
                                    labelColor = Color(0xFF1E293B)
                                ),
                                border = InputChipDefaults.inputChipBorder(
                                    enabled = true,
                                    selected = false,
                                    borderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }
                }

                // Popular Kenya Locations Suggestions
                if (uiState.locationSuggestions.isNotEmpty() && (isSearchFocused || uiState.searchQuery.isNotBlank())) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (uiState.searchQuery.isBlank()) "Popular Locations in Kenya" else "Location Suggestions",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.locationSuggestions, key = { it }) { location ->
                            SuggestionChip(
                                onClick = {
                                    val cleanedLoc = location.split(",").first().trim()
                                    onSearchQueryChange(cleanedLoc)
                                    onSearchSubmit(cleanedLoc)
                                },
                                label = {
                                    Text(
                                        location,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = EmeraldPrimary
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0xFFECFDF5),
                                    labelColor = Color(0xFF065F46)
                                ),
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = Color(0xFFA7F3D0)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category Tabs Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = uiState.selectedCategory.equals(category, ignoreCase = true)
                        val icon = when (category) {
                            "Residential" -> Icons.Default.Home
                            "Commercial" -> Icons.Default.Business
                            "Land" -> Icons.Default.Landscape
                            else -> Icons.Default.HomeWork
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelect(category) },
                            label = {
                                Text(
                                    category,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) Color.White else EmeraldPrimary
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color(0xFF334155)
                            ),
                            border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Location Quick Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(locations) { loc ->
                        val isSelected = uiState.locationFilter.equals(loc, ignoreCase = true)
                        AssistChip(
                            onClick = { onLocationFilterSelect(loc) },
                            label = {
                                Text(
                                    loc,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = if (isSelected) EmeraldPrimary else Color(0xFF94A3B8)
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) SoftBlueHighlight else Color.Transparent
                            ),
                            border = if (isSelected) BorderStroke(1.dp, Color(0xFFBFDBFE)) else BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                        )
                    }
                }
            }
        }

        // Content Area: Map View Mode OR Property List
        if (isMapViewMode) {
            KenyanPropertyMapView(
                properties = properties,
                onPropertyClick = onPropertyClick
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Professional Polish Quick Stats Dashboard Banner Widget
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftBlueHighlight),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "YOUR DASHBOARD",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldPrimary,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$viewingCount Pending Inquiries & Viewings",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = EmeraldPrimary,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "View Inquiries",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (properties.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No properties found matching your criteria",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Try clearing filters or searching for another location like 'Kilimani' or 'Karen'.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        onCategorySelect("All")
                                        onLocationFilterSelect("All")
                                        onSearchQueryChange("")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                                ) {
                                    Text("Reset All Filters")
                                }
                            }
                        }
                    }
                } else {
                    items(properties, key = { it.id }) { property ->
                        PropertyCard(
                            property = property,
                            onPropertyClick = onPropertyClick,
                            onSaveToggle = onSaveToggle
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Boma Real Estate Platform",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                )
                                Text(
                                    text = "Created & Designed by Denis Obere Alumasi",
                                    style = MaterialTheme.typography.bodySmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Verified Land Search • ArdhiSasa Biometric Integration • Title Deed Vault",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KenyanPropertyMapView(
    properties: List<Property>,
    onPropertyClick: (Property) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
    ) {
        // Simulated Interactive Map Canvas representation
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = TerracottaGold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nairobi & Nakuru Regional Map Pins",
                            style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "${properties.size} Pins",
                        style = MaterialTheme.typography.bodySmall.copy(color = TerracottaGold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Map Pins Grid List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(properties) { prop ->
                    val formattedPrice = rememberFormattedKsh(prop.priceKsh)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onPropertyClick(prop) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = CircleShape,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = prop.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = prop.location,
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = TerracottaGold,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "KSh $formattedPrice",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
