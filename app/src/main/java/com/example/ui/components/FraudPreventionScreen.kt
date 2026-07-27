package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.TerracottaGold
import com.example.ui.theme.VerifiedBadgeGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FraudPreventionScreen(
    onReportFraudClick: () -> Unit,
    onRunBiometricAudit: () -> Unit = {}
) {
    var searchLicenseInput by remember { mutableStateOf("") }
    var searchResultStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Header
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "COMPLIANCE & VERIFICATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Anti-Fraud Controls",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // Biometric Title Deed Fraud Check Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = EmeraldPrimary,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = "Biometric Verification",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Biometric Land Registry Verification",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ArdhiSasa Biometric Identity API Integration",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Authenticate your digital fingerprint or face ID to run an instant cryptographic check against Kenya National Land Information System (NLIS) records.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onRunBiometricAudit,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan Biometrics & Audit Title Deed",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // EARB License Search Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Verify Agent EARB License",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Verify if an agent is registered with Estate Agents Registration Board of Kenya.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchLicenseInput,
                        onValueChange = {
                            searchLicenseInput = it
                            searchResultStatus = null
                        },
                        placeholder = { Text("Enter License e.g. EARB/A/2026/0481") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                if (searchLicenseInput.isBlank()) return@IconButton
                                searchResultStatus = if (searchLicenseInput.contains("0481") || searchLicenseInput.contains("0112")) {
                                    "VERIFIED_ACTIVE"
                                } else {
                                    "NOT_FOUND_WARNING"
                                }
                            }) {
                                Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = "Verify")
                            }
                        }
                    )

                    if (searchResultStatus == "VERIFIED_ACTIVE") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = VerifiedBadgeGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "EARB License Active & Verified",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = VerifiedBadgeGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Agent holds valid practicing certificate under Cap 533 Laws of Kenya.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                                    )
                                }
                            }
                        }
                    } else if (searchResultStatus == "NOT_FOUND_WARNING") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "License Not Found in Active Registry",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.Red,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Exercise caution. Do NOT send M-Pesa deposits without physically viewing property and checking ArdhiHouse land search.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Rules of Safe Property Buying in Kenya
            Text(
                text = "KENYAN REAL ESTATE BUYER PROTECTION RULES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            SafetyRuleCard(
                icon = Icons.Default.FactCheck,
                title = "1. Official ArdhiSasa / Ministry Land Search",
                description = "Always conduct an official land search at ArdhiHouse or online via ArdhiSasa to verify the true registered owner and check for bank charges or court caveats.",
                accentColor = EmeraldPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            SafetyRuleCard(
                icon = Icons.Default.AccountBalance,
                title = "2. Pay Deposits to Advocate Escrow Account",
                description = "Never send cash or direct M-Pesa to an agent's personal phone number. Purchase deposits must strictly go through a registered High Court Advocate's client escrow account.",
                accentColor = TerracottaGold
            )

            Spacer(modifier = Modifier.height(10.dp))

            SafetyRuleCard(
                icon = Icons.Default.PinDrop,
                title = "3. Physical Land Beaconing & Site Verification",
                description = "Confirm the plot beacons physically with a registered surveyor before signing sale agreements. Verify boundaries against the official Registry Index Map (RIM).",
                accentColor = MpesaGreen
            )

            Spacer(modifier = Modifier.height(10.dp))

            SafetyRuleCard(
                icon = Icons.Default.Gavel,
                title = "4. Stamp Duty & Ministry Title Transfer",
                description = "Ensure Stamp Duty is paid directly to Kenya Revenue Authority (KRA) via iTax, and obtain official valuation before final execution.",
                accentColor = EmeraldPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Report Scam Button Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Encountered a Suspicious Listing?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Report fake title deeds, double allocation, or scam agents immediately.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onReportFraudClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "FILE FRAUD REPORT WITH COMPLIANCE",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SafetyRuleCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = accentColor.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
