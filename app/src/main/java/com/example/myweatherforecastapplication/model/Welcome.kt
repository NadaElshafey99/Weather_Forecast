package com.example.myweatherforecastapplication.model

data class Welcome (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current,
    val minutely: List<Minutely>,
    val hourly: List<Current>,
    val daily: List<Daily>,
    val alerts: List<Alert>
)