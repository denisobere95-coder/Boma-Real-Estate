package com.example.data.api

import com.squareup.moshi.Json

data class RentCastRentValuationResponse(
    @field:Json(name = "rent") val rent: Double? = null,
    @field:Json(name = "rentRangeLow") val rentRangeLow: Double? = null,
    @field:Json(name = "rentRangeHigh") val rentRangeHigh: Double? = null,
    @field:Json(name = "pricePerSqFt") val pricePerSqFt: Double? = null,
    @field:Json(name = "latitude") val latitude: Double? = null,
    @field:Json(name = "longitude") val longitude: Double? = null,
    @field:Json(name = "subjectProperty") val subjectProperty: RentCastPropertyDetail? = null
)

data class RentCastPropertyDetail(
    @field:Json(name = "formattedAddress") val formattedAddress: String? = null,
    @field:Json(name = "bedrooms") val bedrooms: Int? = null,
    @field:Json(name = "bathrooms") val bathrooms: Double? = null,
    @field:Json(name = "squareFootage") val squareFootage: Int? = null,
    @field:Json(name = "propertyType") val propertyType: String? = null
)

data class RentCastMarketStatsResponse(
    @field:Json(name = "zipCode") val zipCode: String? = null,
    @field:Json(name = "averageRent") val averageRent: Double? = null,
    @field:Json(name = "medianRent") val medianRent: Double? = null,
    @field:Json(name = "minRent") val minRent: Double? = null,
    @field:Json(name = "maxRent") val maxRent: Double? = null,
    @field:Json(name = "totalListings") val totalListings: Int? = null
)
