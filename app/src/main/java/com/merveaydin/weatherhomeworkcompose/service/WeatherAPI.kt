package com.merveaydin.weatherhomeworkcompose.service


import com.merveaydin.weatherhomeworkcompose.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET

interface WeatherAPI {

    @GET("forecast.json?key=bee74818ccf445498b9132557251508&q=İstanbul&days=1&aqi=no&alerts=no")
    fun getData() : Call<List<WeatherModel>>
}