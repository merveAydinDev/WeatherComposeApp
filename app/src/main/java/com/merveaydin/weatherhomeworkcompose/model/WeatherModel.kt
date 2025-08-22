package com.merveaydin.weatherhomeworkcompose.model

data class WeatherModel(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)