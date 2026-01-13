package com.example.weather_ftapp.data

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)
