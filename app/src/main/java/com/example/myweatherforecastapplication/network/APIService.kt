package com.example.myweatherforecastapplication.network

import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.Welcome
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("units") units: String?,
        @Query("lang")language:String?,
        @Query("appid") appid: String?,
    ): Welcome
}