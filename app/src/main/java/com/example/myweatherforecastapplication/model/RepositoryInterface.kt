package com.example.myweatherforecastapplication.model

interface RepositoryInterface {
    suspend fun getWeather(lat:Double,lon:Double):Welcome
}