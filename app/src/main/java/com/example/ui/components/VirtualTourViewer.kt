package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Property
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TerracottaGold

@Composable
fun VirtualTourViewer(
    property: Property,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedRoomIndex by remember { mutableIntStateOf(0) }

    data class RoomTourItem(val name: String, val drawableName: String, val cdnUrl: String)

    val rooms = listOf(
        RoomTourItem("Main Lounge & Balcony", property.imageDrawableName, property.imageUrl.ifBlank { "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80" }),
        RoomTourItem("Master Bedroom En-Suite", "img_karen_villa_1785090845590", "https://images.unsplash.com/photo-1616594039964-ae9021a400a0?auto=format&fit=crop&w=1200&q=80"),
        RoomTourItem("Chef's Fitted Kitchen", "img_nairobi_apartment_1785090831216", "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=1200&q=80"),
        RoomTourItem("Executive Workspace", "img_westlands_office_1785090860061", "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80")
    )

    val currentRoom = rooms[selectedRoomIndex]
    val currentRoomName = currentRoom.name

    val imageResId = context.resources.getIdentifier(
        currentRoom.drawableName,
        "drawable",
        context.packageName
    )

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1.15f) }
    var activeHotspotInfo by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 360 Viewport Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(selectedRoomIndex) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1.0f, 3.0f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            coil.compose.SubcomposeAsyncImage(
                model = currentRoom.cdnUrl,
                contentDescription = "360 Tour View",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                error = {
                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "360 Tour View Fallback",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )

            // Panoramic 360 Compass overlay indicator
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenRotation,
                        contentDescription = null,
                        tint = TerracottaGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "360° VIRTUAL TOUR • DRAG TO PAN & PINCH TO ZOOM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Hotspot Pins on Room
            HotspotPin(
                label = "Natural Light Solar Balcony",
                offset = Offset(-120f, -80f),
                onClick = { activeHotspotInfo = "Double glazed UV-protected floor-to-ceiling glass sliding doors." }
            )

            HotspotPin(
                label = "Smart Access Control Lock",
                offset = Offset(100f, 60f),
                onClick = { activeHotspotInfo = "Biometric fingerprint & M-Pesa passcode unlock door system." }
            )

            HotspotPin(
                label = "Italian Marble Countertop",
                offset = Offset(0f, 150f),
                onClick = { activeHotspotInfo = "Imported Carrara marble island with built-in induction burner." }
            )
        }

        // Top Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Room: $currentRoomName",
                    style = MaterialTheme.typography.bodySmall.copy(color = TerracottaGold)
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Virtual Tour",
                    tint = Color.White
                )
            }
        }

        // Hotspot Popup Dialog overlay
        AnimatedVisibility(
            visible = activeHotspotInfo != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TerracottaGold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Feature Details",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = activeHotspotInfo ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { activeHotspotInfo = null },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("Got It")
                    }
                }
            }
        }

        // Bottom Room Selector Carousel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = "SELECT ROOM TO EXPLORE:",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rooms.forEachIndexed { index, roomItem ->
                    val isSelected = index == selectedRoomIndex
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedRoomIndex = index
                                scale = 1.15f
                                offsetX = 0f
                                offsetY = 0f
                            },
                        color = if (isSelected) EmeraldPrimary else Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = roomItem.name.split(" ").take(2).joinToString(" "),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
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
fun HotspotPin(
    label: String,
    offset: Offset,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset.x.dp, y = offset.y.dp)
                .clip(CircleShape)
                .background(TerracottaGold)
                .clickable { onClick() }
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
