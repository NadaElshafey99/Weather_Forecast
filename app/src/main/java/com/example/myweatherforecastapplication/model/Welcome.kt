package com.example.myweatherforecastapplication.model

import java.io.Serializable


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
): Serializable