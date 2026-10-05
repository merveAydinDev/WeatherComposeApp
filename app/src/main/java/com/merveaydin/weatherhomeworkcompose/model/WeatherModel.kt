package com.merveaydin.weatherhomeworkcompose.model

data class WeatherModel(
    val location: WeatherLocation,
    val current: Current,
    val forecast: Forecast
)
data class Forecast(
    val forecastday: ArrayList<Forecastday>
)
data class Forecastday(
    val date: String,
    val day: Day,
    val hour: ArrayList<Hour>
)
data class Hour(
    val time: String,
    val temp_c: Double,
    val condition: Condition
)
data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: Condition
)
data class WeatherLocation(
    val name: String,
    val localtime: String,
    val country: String
)

data class Current(
    val temp_c: Double,
    val is_day: Int,
    val condition: Condition

)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)
data class SearchLocation(
    val name: String,
    val country: String,
    val url: String
)
