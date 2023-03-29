package com.example.myweatherforecastapplication.network

import com.example.myweatherforecastapplication.model.Welcome

interface RemoteSourceInterface {
    suspend fun getCurrentWeather(lat:Double,lon:Double): Welcome
}