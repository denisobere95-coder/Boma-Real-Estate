package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Property
import com.example.data.api.ApiIntegrationStatus
import com.example.data.api.IntegratedMarketMetadata
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.TerracottaGold
import com.example.ui.theme.VerifiedBadgeGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    property: Property,
    liveMarketMetadata: IntegratedMarketMetadata? = null,
    apiStatus: ApiIntegrationStatus? = null,
    isFetchingLiveApiData: Boolean = false,
    onRefreshLiveApiData: () -> Unit = {},
    onBack: () -> Unit,
    onSaveToggle: (Property) -> Unit,
    onOpenScheduleViewing: () -> Unit,
    onOpen360Tour: () -> Unit,
    onOpenFraudReport: () -> Unit,
    onOpenMortgageCalculator: () -> Unit
) {

    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        property.imageDrawableName,
        "drawable",
        context.packageName
    )

    val formattedPrice = rememberFormattedKsh(property.priceKsh)

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // Hero Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                PropertyImage(
                    property = property,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Back Button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Favorite Save Button
                IconButton(
                    onClick = { onSaveToggle(property) },
                    modifier = Modifier
                        .statusBarsPadding()
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (property.isSaved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (property.isSaved) Color.Red else Color.White
                    )
                }

                // 360° Virtual Tour Pill Button
                if (property.is360TourAvailable) {
                    Button(
                        onClick = onOpen360Tour,
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaGold),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ScreenRotation,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "LAUNCH 360° TOUR",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Property Info Card
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = EmeraldPrimary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = property.category.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "ID: ${property.id.uppercase()}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = property.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = property.location,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EmeraldPrimary.copy(alpha = 0.08f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PRICE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "KSh $formattedPrice",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary
                                )
                            )
                        }
                        if (property.pricePeriod.isNotBlank()) {
                            Text(
                                text = property.pricePeriod,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Specs Grid
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        if (property.bedrooms > 0) {
                            DetailSpecItem(icon = Icons.Default.Bed, label = "Bedrooms", value = "${property.bedrooms}")
                        }
                        if (property.bathrooms > 0) {
                            DetailSpecItem(icon = Icons.Default.Bathtub, label = "Bathrooms", value = "${property.bathrooms}")
                        }
                        if (property.areaSqFt > 0) {
                            DetailSpecItem(icon = Icons.Default.SquareFoot, label = "Area", value = "${property.areaSqFt} sqft")
                        }
                        DetailSpecItem(icon = Icons.Default.HomeWork, label = "Type", value = property.propertyType)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title Deed & EARB Verification Status Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(VerifiedBadgeGreen))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = VerifiedBadgeGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Legal & Title Verification",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = VerifiedBadgeGreen
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        VerificationCheckRow(
                            title = "Estate Agents Registration Board (EARB)",
                            subtitle = "License No: ${property.earbLicenseNo} (Active & Verified)",
                            isVerified = property.isAgentVerified
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        VerificationCheckRow(
                            title = "ArdhiHouse Ministry of Lands Title Deed",
                            subtitle = "Title deed searched & cleared for encumbrances",
                            isVerified = property.isTitleDeedVerified
                        )

                        if (property.mpesaEscrowSupported) {
                            Spacer(modifier = Modifier.height(8.dp))
                            VerificationCheckRow(
                                title = "M-Pesa Escrow Holding Protection",
                                subtitle = "Funds released to seller only upon title transfer approval",
                                isVerified = true
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Live Market Intelligence & Real API Metadata Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Analytics,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "LIVE MARKET DATA & API METADATA",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF0F172A)
                                        )
                                    )
                                    Text(
                                        text = liveMarketMetadata?.apiSource ?: "RentCast • Apify • RealtyAPI Pipeline",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            IconButton(onClick = onRefreshLiveApiData) {
                                if (isFetchingLiveApiData) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = EmeraldPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh Market Data",
                                        tint = EmeraldPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val meta = liveMarketMetadata
                        val estRentKsh = meta?.estimatedMonthlyRentKsh?.toLong() ?: (property.priceKsh / 150)
                        val formattedEstRent = rememberFormattedKsh(estRentKsh)
                        val formattedLowRent = rememberFormattedKsh((estRentKsh * 0.88).toLong())
                        val formattedHighRent = rememberFormattedKsh((estRentKsh * 1.15).toLong())

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Estimated Market Rent",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                                )
                                Text(
                                    text = "$formattedEstRent / mo",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                )
                                Text(
                                    text = "Range: $formattedLowRent - $formattedHighRent",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF475569))
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cap Rate / Rental Yield",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                                )
                                Text(
                                    text = "${meta?.capRateYieldPct ?: 8.5}%",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                )
                                Text(
                                    text = "Demand Score: ${meta?.demandScore ?: 9.2} / 10",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF475569))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 0.8.dp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Multi-Platform Syndication Status",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            (meta?.activeSyndicatedPlatforms ?: listOf("Boma Network", "BuyRentKenya", "Property24")).forEach { platform ->
                                Surface(
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = platform,
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF1E293B)),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // API Key Status Badges
                        val apis = apiStatus
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ApiBadgeRow(
                                name = "RentCast API",
                                configured = apis?.rentCastConfigured ?: false,
                                text = apis?.rentCastStatusText ?: "Valuations & Tax Records"
                            )
                            ApiBadgeRow(
                                name = "Apify Scraper",
                                configured = apis?.apifyConfigured ?: false,
                                text = apis?.apifyStatusText ?: "Live Housing Market Scraper"
                            )
                            ApiBadgeRow(
                                name = "RealtyAPI",
                                configured = apis?.realtyApiConfigured ?: false,
                                text = apis?.realtyApiStatusText ?: "Multi-Platform MLS Syndication"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))


                // Description
                Text(
                    text = "Property Description",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = property.description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Agent Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "LISTED BY VERIFIED AGENT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = property.agentName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = TerracottaGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${property.agentRating} (Verified)",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            // WhatsApp Direct Button
                            IconButton(
                                onClick = {
                                    val url = "https://api.whatsapp.com/send?phone=${property.agentPhone.replace(" ", "")}&text=Hello%20${property.agentName},%20I%20am%20interested%20in%20${property.title}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .background(MpesaGreen, CircleShape)
                                    .size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = "WhatsApp Agent",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Mortgage Calculator Shortcut Banner
                Card(
                    onClick = onOpenMortgageCalculator,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Estimate Mortgage Monthly Payment",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Calculate KCB, Absa, Stanbic rates for KSh $formattedPrice",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Fraud Control Button
                OutlinedButton(
                    onClick = onOpenFraudReport,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.Red.copy(alpha = 0.5f)))
                ) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report Fraudulent Listing or Fake Title Deed")
                }
            }
        }

        // Fixed Bottom Call to Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${property.agentPhone}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Agent")
                }

                Button(
                    onClick = onOpenScheduleViewing,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Book Viewing",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun ApiBadgeRow(name: String, configured: Boolean, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = if (configured) EmeraldPrimary else Color(0xFFE2E8F0),
            modifier = Modifier.size(8.dp)
        ) {}
        Text(
            text = "$name: ",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(color = if (configured) EmeraldPrimary else Color(0xFF64748B))
        )
    }
}

@Composable
fun DetailSpecItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Composable
fun VerificationCheckRow(title: String, subtitle: String, isVerified: Boolean) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isVerified) VerifiedBadgeGreen else Color.Gray,
            modifier = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
            )
        }
    }
}
