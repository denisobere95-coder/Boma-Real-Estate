package com.example.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RentCastService {

    @GET("valuations/rent")
    suspend fun getRentValuation(
        @Header("X-Api-Key") apiKey: String,
        @Query("address") address: String,
        @Query("bedrooms") bedrooms: Int? = null,
        @Query("bathrooms") bathrooms: Double? = null,
        @Query("squareFootage") squareFootage: Int? = null
    ): Response<RentCastRentValuationResponse>

    @GET("markets")
    suspend fun getMarketStats(
        @Header("X-Api-Key") apiKey: String,
        @Query("zipCode") zipCode: String
    ): Response<RentCastMarketStatsResponse>
}
