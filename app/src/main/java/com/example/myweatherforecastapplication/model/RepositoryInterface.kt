package com.example.myweatherforecastapplication.model

interface RepositoryInterface {
    suspend fun getWeather():Welcome
//    suspend fun getHourlyWeather():List<Current>
}