package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mortgage_leads")
data class MortgageLead(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val propertyTitle: String,
    val propertyPriceKsh: Long,
    val depositAmountKsh: Long,
    val loanTermYears: Int,
    val interestRatePercent: Double,
    val estimatedMonthlyPaymentKsh: Long,
    val preferredBank: String, // "KCB Bank Kenya", "Absa Kenya", "Stanbic Bank", "NCBA Bank"
    val applicantName: String,
    val applicantPhone: String,
    val applicantEmail: String,
    val status: String = "Lead Submitted to Bank",
    val timestamp: Long = System.currentTimeMillis()
)
