package com.example.data.api

import com.squareup.moshi.Json

data class RealtyListingSearchResponse(
    @field:Json(name = "totalResults") val totalResults: Int? = null,
    @field:Json(name = "listings") val listings: List<RealtyListingItem>? = null
)

data class RealtyListingItem(
    @field:Json(name = "mlsId") val mlsId: String? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "address") val address: String? = null,
    @field:Json(name = "price") val price: Long? = null,
    @field:Json(name = "syndicatedPlatforms") val syndicatedPlatforms: List<String>? = null,
    @field:Json(name = "lastUpdated") val lastUpdated: String? = null
)
