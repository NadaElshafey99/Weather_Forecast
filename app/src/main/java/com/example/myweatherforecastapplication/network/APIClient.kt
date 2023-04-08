package com.example.myweatherforecastapplication.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.PreferenceHelper.temperatureUnit
import com.example.myweatherforecastapplication.PreferenceHelper.unit
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Welcome

class APIClient private constructor() : RemoteSourceInterface {
    private val apiService: APIService by lazy {
        RetrofitHelper.getInstance().create(APIService::class.java)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double, context: Context): Welcome {
        val prefs = PreferenceHelper.customPreference(context, CUSTOM_PREF_NAME)
        return apiService.getCurrentWeather(
            lat,
            lon,
            prefs.unit?:"standard",
            prefs.language?:"en",
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