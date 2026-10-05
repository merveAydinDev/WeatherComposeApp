package com.merveaydin.weatherhomeworkcompose.service

import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int
    ): WeatherModel

}

