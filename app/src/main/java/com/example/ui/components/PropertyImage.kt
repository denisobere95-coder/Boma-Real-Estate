package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.Property
import com.example.ui.theme.EmeraldPrimary

/**
 * Universal Property Image Composable that renders property/land/apartment/room images
 * seamlessly across all browsers and devices. It uses Coil to stream high-resolution
 * web CDN images (Unsplash) with fallbacks to local drawables or type-specific vector graphics.
 */
@Composable
fun PropertyImage(
    property: Property,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = property.title
) {
    val context = LocalContext.current

    // Resolve universal image URL or default CDN URL based on property type
    val cdnUrl = when {
        property.imageUrl.isNotBlank() -> property.imageUrl
        property.propertyType.contains("Apartment", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=1200&q=80"
        property.propertyType.contains("Villa", ignoreCase = true) || property.propertyType.contains("House", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1200&q=80"
        property.propertyType.contains("Plot", ignoreCase = true) || property.propertyType.contains("Land", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=80"
        property.propertyType.contains("Office", ignoreCase = true) || property.propertyType.contains("Retail", ignoreCase = true) ->
            "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=1200&q=80"
        else -> "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80"
    }

    val localResId = if (property.imageDrawableName.isNotBlank()) {
        context.resources.getIdentifier(
            property.imageDrawableName,
            "drawable",
            context.packageName
        )
    } else 0

    val imageModel = when {
        cdnUrl.isNotBlank() -> cdnUrl
        localResId != 0 -> localResId
        else -> null
    }

    if (imageModel != null) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = EmeraldPrimary,
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                // If remote network fails, try local drawable or icon placeholder
                if (localResId != 0) {
                    Image(
                        painter = painterResource(id = localResId),
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = contentScale
                    )
                } else {
                    PropertyTypePlaceholder(
                        propertyType = property.propertyType,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    } else {
        PropertyTypePlaceholder(
            propertyType = property.propertyType,
            modifier = modifier
        )
    }
}

@Composable
fun PropertyTypePlaceholder(
    propertyType: String,
    modifier: Modifier = Modifier
) {
    val icon = when {
        propertyType.contains("Plot", ignoreCase = true) || propertyType.contains("Land", ignoreCase = true) ->
            Icons.Default.Landscape
        propertyType.contains("Office", ignoreCase = true) || propertyType.contains("Retail", ignoreCase = true) ->
            Icons.Default.Business
        propertyType.contains("Apartment", ignoreCase = true) ->
            Icons.Default.Apartment
        else -> Icons.Default.Home
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(48.dp)
        )
    }
}
