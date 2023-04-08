package com.example.myweatherforecastapplication.network

import android.content.Context
import com.example.myweatherforecastapplication.model.Welcome

interface RemoteSourceInterface {
    suspend fun getCurrentWeather(lat:Double,lon:Double,context: Context): Welcome
}