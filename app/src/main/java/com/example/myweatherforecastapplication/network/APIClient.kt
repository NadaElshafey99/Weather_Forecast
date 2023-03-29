package com.example.myweatherforecastapplication.network

import android.content.SharedPreferences
import com.example.myweatherforecastapplication.model.Welcome

class APIClient private constructor() : RemoteSourceInterface {
    private val apiService: APIService by lazy {
        RetrofitHelper.getInstance().create(APIService::class.java)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Welcome {
        return apiService.getCurrentWeather(
            lat,
            lon,
            "metric",
            "ca201421061f034c889bb5b55aec30f8"
        )
    }

    companion object {
        private var instance: APIClient? = null
        fun getInstance(): APIClient {
            return instance ?: synchronized(this)
            {
                val temp = APIClient()
                instance = temp
                temp
            }
        }
    }

}