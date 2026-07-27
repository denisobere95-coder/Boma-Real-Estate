package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_vault")
data class DocumentVaultItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val docType: String, // "Title Deed", "Sale Agreement", "Land Registry Verification", "Lease Contract"
    val docName: String,
    val propertyTitle: String,
    val dateAdded: String,
    val fileSize: String,
    val status: String = "Encrypted & Verified", // "Encrypted & Verified", "Under Review", "Pending Stamp Duty"
    val verificationHash: String = "0x8F92...B3C1"
)
