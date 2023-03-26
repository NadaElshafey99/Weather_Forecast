package com.example.myweatherforecastapplication.model

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val weather: List<Weather>,
    val rain: Rain? = null,
    val windGust: Double? = null,
    val pop: Long? = null
)