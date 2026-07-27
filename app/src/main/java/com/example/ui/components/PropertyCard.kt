package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Property
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TerracottaGold
import com.example.ui.theme.VerifiedBadgeGreen
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyCard(
    property: Property,
    onPropertyClick: (Property) -> Unit,
    onSaveToggle: (Property) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        property.imageDrawableName,
        "drawable",
        context.packageName
    )

    val formattedPrice = rememberFormattedKsh(property.priceKsh)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onPropertyClick(property) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                PropertyImage(
                    property = property,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                )

                // Category & Badges on Top Left
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldPrimary,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = property.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (property.is360TourAvailable) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TerracottaGold,
                            contentColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ScreenRotation,
                                    contentDescription = "360 Tour",
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "360° TOUR",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Save Favorite Button Top Right
                IconButton(
                    onClick = { onSaveToggle(property) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                        .testTag("favorite_toggle_${property.id}")
                ) {
                    val iconTint by animateColorAsState(
                        if (property.isSaved) Color.Red else Color.White,
                        label = "save_color"
                    )
                    Icon(
                        imageVector = if (property.isSaved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save Property",
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Price Tag Bottom Left
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "KSh $formattedPrice",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    if (property.pricePeriod.isNotBlank()) {
                        Text(
                            text = " ${property.pricePeriod}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f)),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // Card Body Content
            Column(modifier = Modifier.padding(14.dp)) {
                // Title
                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location with pin icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = property.location,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Specs (Beds, Baths, SqFt)
                if (property.bedrooms > 0 || property.bathrooms > 0 || property.areaSqFt > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (property.bedrooms > 0) {
                            SpecBadge(icon = Icons.Default.Bed, text = "${property.bedrooms} Beds")
                        }
                        if (property.bathrooms > 0) {
                            SpecBadge(icon = Icons.Default.Bathtub, text = "${property.bathrooms} Baths")
                        }
                        if (property.areaSqFt > 0) {
                            SpecBadge(icon = Icons.Default.SquareFoot, text = "${property.areaSqFt} sqft")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(8.dp))

                // Verification Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Agent",
                            tint = VerifiedBadgeGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "EARB Licensed Agent",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = VerifiedBadgeGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    if (property.isTitleDeedVerified) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Title Verified",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "ArdhiHouse Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun rememberFormattedKsh(amount: Long): String {
    return try {
        val format = NumberFormat.getNumberInstance(Locale.US)
        format.format(amount)
    } catch (e: Exception) {
        amount.toString()
    }
}
