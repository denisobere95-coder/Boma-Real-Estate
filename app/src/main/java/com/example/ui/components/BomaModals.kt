package com.example.ui.components

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
import androidx.compose.ui.window.Dialog
import com.example.data.Property
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TerracottaGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleViewingModal(
    property: Property,
    onDismiss: () -> Unit,
    onSubmit: (date: String, timeSlot: String, name: String, phone: String) -> Unit
) {
    var dateInput by remember { mutableStateOf("2026-07-28") }
    var timeSlotInput by remember { mutableStateOf("10:00 AM - 11:30 AM") }
    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }

    val timeSlots = listOf(
        "09:00 AM - 10:30 AM",
        "11:00 AM - 12:30 PM",
        "02:00 PM - 03:30 PM",
        "04:00 PM - 05:30 PM"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Book Property Viewing",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = property.title,
                    style = MaterialTheme.typography.bodyMedium.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("Preferred Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Select Time Slot:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    timeSlots.forEach { slot ->
                        val isSelected = slot == timeSlotInput
                        FilterChip(
                            selected = isSelected,
                            onClick = { timeSlotInput = slot },
                            label = { Text(slot) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Your Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("M-Pesa Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (nameInput.isNotBlank() && phoneInput.isNotBlank()) {
                            onSubmit(dateInput, timeSlotInput, nameInput, phoneInput)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("CONFIRM VIEWING BOOKING")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FraudReportModal(
    property: Property,
    onDismiss: () -> Unit,
    onSubmit: (reason: String, details: String) -> Unit
) {
    var reasonInput by remember { mutableStateOf("Suspected Fake Title Deed") }
    var detailsInput by remember { mutableStateOf("") }

    val reasons = listOf(
        "Suspected Fake Title Deed",
        "Agent Demanding M-Pesa Cash Deposit Before Viewing",
        "Double Allocation / Land Boundary Dispute",
        "False Pictures / Misleading Property Description"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Report Fraud",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.Red)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Property: ${property.title}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Primary Violation Reason:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    reasons.forEach { r ->
                        val isSelected = r == reasonInput
                        FilterChip(
                            selected = isSelected,
                            onClick = { reasonInput = r },
                            label = { Text(r, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.Red,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = detailsInput,
                    onValueChange = { detailsInput = it },
                    label = { Text("Provide Specific Incident Details") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onSubmit(reasonInput, detailsInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("SUBMIT REPORT TO COMPLIANCE")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingModal(
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Residential") }
    var typeInput by remember { mutableStateOf("Apartment") }
    var priceInput by remember { mutableStateOf("") }
    var periodInput by remember { mutableStateOf("Total Price") }
    var locationInput by remember { mutableStateOf("Kilimani, Nairobi") }
    var bedroomsInput by remember { mutableStateOf("3") }
    var bathroomsInput by remember { mutableStateOf("2") }
    var sqftInput by remember { mutableStateOf("1500") }
    var descriptionInput by remember { mutableStateOf("") }
    var agentNameInput by remember { mutableStateOf("") }
    var agentPhoneInput by remember { mutableStateOf("") }

    val categories = listOf("Residential", "Commercial", "Land")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Publish Property Listing",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Property Title") },
                    placeholder = { Text("e.g. 3 BR Apartment in Kilimani") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = cat == categoryInput,
                            onClick = { categoryInput = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceInput,
                        onValueChange = { priceInput = it },
                        label = { Text("Price (KSh)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = locationInput,
                        onValueChange = { locationInput = it },
                        label = { Text("Location") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bedroomsInput,
                        onValueChange = { bedroomsInput = it },
                        label = { Text("Beds") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bathroomsInput,
                        onValueChange = { bathroomsInput = it },
                        label = { Text("Baths") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sqftInput,
                        onValueChange = { sqftInput = it },
                        label = { Text("SqFt") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = { descriptionInput = it },
                    label = { Text("Property Description & Features") },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = agentNameInput,
                        onValueChange = { agentNameInput = it },
                        label = { Text("Agent Name") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = agentPhoneInput,
                        onValueChange = { agentPhoneInput = it },
                        label = { Text("Agent Phone") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val pKsh = priceInput.toLongOrNull() ?: 10_000_000L
                        val b = bedroomsInput.toIntOrNull() ?: 3
                        val ba = bathroomsInput.toIntOrNull() ?: 2
                        val sq = sqftInput.toIntOrNull() ?: 1200

                        if (titleInput.isNotBlank()) {
                            onSubmit(
                                titleInput,
                                categoryInput,
                                typeInput,
                                pKsh,
                                periodInput,
                                locationInput,
                                b,
                                ba,
                                sq,
                                descriptionInput,
                                agentNameInput,
                                agentPhoneInput
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("PUBLISH PROPERTY LISTING")
                }
            }
        }
    }
}

@Composable
fun AddDocumentModal(
    onDismiss: () -> Unit,
    onSubmit: (docType: String, docName: String, propertyTitle: String) -> Unit
) {
    var docNameInput by remember { mutableStateOf("") }
    var docTypeInput by remember { mutableStateOf("Title Deed") }
    var propTitleInput by remember { mutableStateOf("Kilimani Horizon Apartment") }

    val docTypes = listOf("Title Deed", "Land Search Certificate", "Lease Contract", "Survey Map")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upload Title Vault Document",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = docNameInput,
                    onValueChange = { docNameInput = it },
                    label = { Text("Document File Name") },
                    placeholder = { Text("e.g. Kilimani_IR_TitleDeed.pdf") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Document Type:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    docTypes.forEach { type ->
                        FilterChip(
                            selected = type == docTypeInput,
                            onClick = { docTypeInput = type },
                            label = { Text(type) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = propTitleInput,
                    onValueChange = { propTitleInput = it },
                    label = { Text("Associated Property Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (docNameInput.isNotBlank()) {
                            onSubmit(docTypeInput, docNameInput, propTitleInput)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("SECURELY STORE IN VAULT")
                }
            }
        }
    }
}
