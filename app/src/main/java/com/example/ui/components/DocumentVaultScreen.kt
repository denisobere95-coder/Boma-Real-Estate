package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.DocumentVaultItem
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TerracottaGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentVaultScreen(
    documents: List<DocumentVaultItem>,
    isVaultUnlocked: Boolean = false,
    onAuthenticateBiometric: () -> Unit = {},
    onLockVault: () -> Unit = {},
    onAddDocumentClick: () -> Unit,
    onSubmitMortgageLead: (
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
    ) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Document Vault, 1: Mortgage Calculator

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "MORTGAGE & VAULT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B),
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "Document Storage",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F172A)
                                )
                            )
                        }
                    }

                    if (selectedSubTab == 0) {
                        SmallFloatingActionButton(
                            onClick = onAddDocumentClick,
                            containerColor = EmeraldPrimary,
                            contentColor = Color.White
                        ) {
                            Icon(imageVector = Icons.Default.UploadFile, contentDescription = "Add Document")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Segmented Tab Toggle
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedSubTab == 0,
                        onClick = { selectedSubTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Title Vault (${documents.size})")
                        }
                    }
                    SegmentedButton(
                        selected = selectedSubTab == 1,
                        onClick = { selectedSubTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mortgage Leads")
                        }
                    }
                }
            }
        }

        if (selectedSubTab == 0) {
            // Document Vault View with Biometric Protection
            Column(modifier = Modifier.fillMaxSize()) {
                // Biometric Security Lock Status Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isVaultUnlocked) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isVaultUnlocked) Color(0xFFBBF7D0) else Color(0xFFFECACA)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = if (isVaultUnlocked) Color(0xFF16A34A) else Color(0xFFDC2626),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isVaultUnlocked) Icons.Default.Fingerprint else Icons.Default.Lock,
                                    contentDescription = "Biometric Lock",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isVaultUnlocked) "Biometric Protection Active" else "Vault Biometrically Locked",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isVaultUnlocked) Color(0xFF14532D) else Color(0xFF7F1D1D)
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isVaultUnlocked) "Sensitive Title Deeds & Legal Documents Unlocked" else "Biometric Scan required to view official title deeds & contracts.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isVaultUnlocked) Color(0xFF15803D) else Color(0xFF991B1B)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        if (!isVaultUnlocked) {
                            Button(
                                onClick = onAuthenticateBiometric,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Authenticate", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            }
                        } else {
                            OutlinedButton(
                                onClick = onLockVault,
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lock Vault", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }

                if (documents.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your Encrypted Title Vault is Empty",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upload Title Deeds, Land Search Certificates, or Lease Agreements for secure encrypted storage.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onAddDocumentClick,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                            ) {
                                Icon(imageVector = Icons.Default.UploadFile, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Upload Document to Vault")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(documents) { doc ->
                            DocumentVaultCard(
                                doc = doc,
                                isUnlocked = isVaultUnlocked,
                                onAuthenticateClick = onAuthenticateBiometric
                            )
                        }
                    }
                }
            }
        } else {
            // Mortgage Calculator View
            MortgageCalculatorView(onSubmitMortgageLead = onSubmitMortgageLead)
        }
    }
}

@Composable
fun DocumentVaultCard(
    doc: DocumentVaultItem,
    isUnlocked: Boolean = false,
    onAuthenticateClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isUnlocked) EmeraldPrimary.copy(alpha = 0.15f) else Color(0xFFFEF2F2),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.Description else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) EmeraldPrimary else Color(0xFFEF4444),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doc.docName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${doc.docType} • ${doc.propertyTitle}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (isUnlocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${doc.status} (${doc.verificationHash})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Encrypted • Biometric Scan to View",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            if (isUnlocked) {
                IconButton(onClick = { /* Open doc preview */ }) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Download Document",
                        tint = EmeraldPrimary
                    )
                }
            } else {
                IconButton(onClick = onAuthenticateClick) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Authenticate Biometrics",
                        tint = EmeraldPrimary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MortgageCalculatorView(
    onSubmitMortgageLead: (
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
    ) -> Unit
) {
    var propertyPriceInput by remember { mutableStateOf("18500000") }
    var depositPercentInput by remember { mutableFloatStateOf(0.20f) } // 20%
    var loanTermYearsInput by remember { mutableIntStateOf(15) } // 15 years
    var selectedBankIndex by remember { mutableIntStateOf(0) }

    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }

    val banks = listOf(
        "KCB Bank Kenya" to 13.5,
        "Absa Kenya" to 13.8,
        "Stanbic Bank Kenya" to 14.0,
        "NCBA Bank Kenya" to 13.2
    )

    val selectedBankName = banks[selectedBankIndex].first
    val selectedRate = banks[selectedBankIndex].second

    val propertyPrice = propertyPriceInput.toLongOrNull() ?: 18_500_000L
    val depositAmount = (propertyPrice * depositPercentInput).toLong()
    val loanAmount = (propertyPrice - depositAmount).coerceAtLeast(0L)

    // Calculate monthly payment formula: M = P [ r(1+r)^n ] / [ (1+r)^n – 1 ]
    val monthlyRate = (selectedRate / 100.0) / 12.0
    val totalMonths = loanTermYearsInput * 12
    val estimatedMonthlyPayment = if (loanAmount > 0 && monthlyRate > 0) {
        val factor = Math.pow(1.0 + monthlyRate, totalMonths.toDouble())
        ((loanAmount * monthlyRate * factor) / (factor - 1.0)).toLong()
    } else {
        0L
    }

    val formattedPrice = rememberFormattedKsh(propertyPrice)
    val formattedDeposit = rememberFormattedKsh(depositAmount)
    val formattedLoan = rememberFormattedKsh(loanAmount)
    val formattedMonthly = rememberFormattedKsh(estimatedMonthlyPayment)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Kenyan Bank Mortgage Calculator",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = propertyPriceInput,
                    onValueChange = { propertyPriceInput = it },
                    label = { Text("Property Price (KSh)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("KSh ") }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Down Payment Deposit: ${(depositPercentInput * 100).toInt()}% (KSh $formattedDeposit)",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Slider(
                    value = depositPercentInput,
                    onValueChange = { depositPercentInput = it },
                    valueRange = 0.10f..0.50f,
                    steps = 8
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Loan Repayment Term: $loanTermYearsInput Years",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Slider(
                    value = loanTermYearsInput.toFloat(),
                    onValueChange = { loanTermYearsInput = it.toInt() },
                    valueRange = 5f..25f,
                    steps = 20
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Partner Commercial Bank:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    banks.forEachIndexed { idx, pair ->
                        val isSelected = idx == selectedBankIndex
                        Card(
                            onClick = { selectedBankIndex = idx },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary)) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pair.first,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${pair.second}% Interest",
                                    style = MaterialTheme.typography.bodySmall.copy(
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

        Spacer(modifier = Modifier.height(16.dp))

        // Results Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = EmeraldPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ESTIMATED MONTHLY REPAYMENT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "KSh $formattedMonthly / mo",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TerracottaGold,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Loan Amount", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        Text("KSh $formattedLoan", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Bank Partner", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        Text(selectedBankName, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Pre-Qualification Lead Submission Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Apply for Bank Pre-Qualification",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Submit your details to $selectedBankName for instant mortgage pre-approval check.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Phone Number (+254...)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nameInput.isNotBlank() && phoneInput.isNotBlank()) {
                            onSubmitMortgageLead(
                                "Property KSh $formattedPrice",
                                propertyPrice,
                                depositAmount,
                                loanTermYearsInput,
                                selectedRate,
                                estimatedMonthlyPayment,
                                selectedBankName,
                                nameInput,
                                phoneInput,
                                emailInput
                            )
                            nameInput = ""
                            phoneInput = ""
                            emailInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SUBMIT MORTGAGE LEAD TO $selectedBankName")
                }
            }
        }
    }
}
