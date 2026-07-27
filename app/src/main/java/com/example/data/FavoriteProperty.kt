package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_properties")
data class FavoriteProperty(
    @PrimaryKey val propertyId: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)
