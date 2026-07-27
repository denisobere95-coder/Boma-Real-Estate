package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewing_requests")
data class ViewingRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val propertyId: String,
    val propertyTitle: String,
    val agentName: String,
    val date: String,
    val timeSlot: String,
    val clientName: String,
    val clientPhone: String,
    val status: String = "Confirmed", // "Pending", "Confirmed", "Completed", "Cancelled"
    val timestamp: Long = System.currentTimeMillis()
)
