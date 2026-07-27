package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApifyService {

    @POST("acts/{actorId}/runs")
    suspend fun triggerHousingScraper(
        @Path("actorId") actorId: String,
        @Query("token") token: String,
        @Body request: ApifyScraperRequest
    ): Response<ApifyRunResponse>

    @GET("datasets/{datasetId}/items")
    suspend fun getDatasetItems(
        @Path("datasetId") datasetId: String,
        @Query("token") token: String
    ): Response<List<ApifyHousingStatItem>>
}
