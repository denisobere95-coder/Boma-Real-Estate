package com.example.data.api

import com.squareup.moshi.Json

data class ApifyScraperRequest(
    @field:Json(name = "location") val location: String,
    @field:Json(name = "maxItems") val maxItems: Int = 20,
    @field:Json(name = "includeMarketStats") val includeMarketStats: Boolean = true
)

data class ApifyRunResponse(
    @field:Json(name = "data") val data: ApifyRunData? = null
)

data class ApifyRunData(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "status") val status: String? = null,
    @field:Json(name = "defaultDatasetId") val defaultDatasetId: String? = null
)

data class ApifyHousingStatItem(
    @field:Json(name = "region") val region: String? = null,
    @field:Json(name = "avgPriceKsh") val avgPriceKsh: Long? = null,
    @field:Json(name = "demandIndex") val demandIndex: Double? = null,
    @field:Json(name = "rentalYieldPct") val rentalYieldPct: Double? = null,
    @field:Json(name = "scrapedAt") val scrapedAt: String? = null
)
