package com.merveaydin.weatherhomeworkcompose.model

data class WeatherModel(
    val location: Location,
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
data class Location(
    val name: String,
    val localtime: String
)

data class Current(
    val temp_c: Double,
    val is_day: Int,
    val condition: Condition

)

data class Condition(
    val text: String,
    val icon: String
)