package com.example.myweatherforecastapplication.network

import com.example.myweatherforecastapplication.model.Welcome

class APIClient private constructor() : RemoteSourceInterface {
    private val apiService: APIService by lazy {
        RetrofitHelper.getInstance().create(APIService::class.java)
    }

    override suspend fun getCurrentWeather(): Welcome {
        return apiService.getCurrentWeather(
            33.44,
            -94.04,
            "metric",
            "4a059725f93489b95183bbcb8c6829b9"
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