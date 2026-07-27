package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "Buy", "Rent", "Land", "Commercial"
    val propertyType: String, // "Apartment", "Villa / House", "Plot / Land", "Office / Retail"
    val priceKsh: Long,
    val pricePeriod: String = "", // e.g., "/ month", "Total price"
    val location: String, // e.g. "Kilimani, Nairobi", "Karen, Nairobi", "Naivasha, Nakuru", "Westlands, Nairobi"
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val areaSqFt: Int = 0,
    val description: String,
    val isAgentVerified: Boolean = true,
    val isTitleDeedVerified: Boolean = true,
    val earbLicenseNo: String = "EARB/A/2026/0481",
    val agentName: String = "David Mutua",
    val agentPhone: String = "+254 712 345 678",
    val agentRating: Double = 4.9,
    val agentAvatarResId: Int = 0,
    val imageDrawableName: String = "", // e.g., "img_nairobi_apartment_1785090831216"
    val imageUrl: String = "", // High-resolution web CDN image accessible across all browsers
    val is360TourAvailable: Boolean = true,
    val isSaved: Boolean = false,
    val fraudReportCount: Int = 0,
    val mpesaEscrowSupported: Boolean = true
)
