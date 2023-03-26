package com.example.myweatherforecastapplication.model

import com.example.myweatherforecastapplication.network.RemoteSourceInterface

class Repository(
    var remoteSource: RemoteSourceInterface
) : RepositoryInterface {
    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSourceInterface
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(remoteSource)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getWeather(): Welcome {
       return remoteSource.getCurrentWeather()
    }

   /* override suspend fun getHourlyWeather(): List<Current> {
        return remoteSource.getCurrentWeather()
    }*/
}