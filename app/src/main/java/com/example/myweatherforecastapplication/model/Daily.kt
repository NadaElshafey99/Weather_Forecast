package com.example.myweatherforecastapplication.model

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)