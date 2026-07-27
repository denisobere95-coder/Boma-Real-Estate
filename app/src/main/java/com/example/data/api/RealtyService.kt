package com.example.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RealtyService {

    @GET("listings/search")
    suspend fun searchMultiPlatformListings(
        @Header("Authorization") bearerToken: String,
        @Query("location") location: String,
        @Query("limit") limit: Int = 10
    ): Response<RealtyListingSearchResponse>
}
