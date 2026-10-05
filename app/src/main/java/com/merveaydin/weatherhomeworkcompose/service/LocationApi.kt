package com.merveaydin.weatherhomeworkcompose.service

import com.merveaydin.weatherhomeworkcompose.model.SearchLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("search.json")
    suspend fun getLocationApi(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): List<SearchLocation>
}

