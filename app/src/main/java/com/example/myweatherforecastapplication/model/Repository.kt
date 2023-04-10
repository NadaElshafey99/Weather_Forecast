package com.example.myweatherforecastapplication.model

import android.content.Context
import com.example.myweatherforecastapplication.db.LocalSourceInterface
import com.example.myweatherforecastapplication.network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow

class Repository(
    var remoteSource: RemoteSourceInterface,
    var localSource:LocalSourceInterface
) : RepositoryInterface {
    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSourceInterface,
            localSource: LocalSourceInterface
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(remoteSource,localSource)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getWeather(lat:Double,lon:Double,context: Context): Welcome {
       return remoteSource.getCurrentWeather(lat,lon,context)
    }

    override suspend fun getFavoritesDB(): Flow<List<Welcome>> {
        return localSource.getAllFavorites()
    }

    override suspend fun addFavoriteToDB(welcome: Welcome) {
        localSource.insertFavorite(welcome)
    }

    override suspend fun updateFavoriteToDB(welcome: Welcome) {
        localSource.updateFavorite(welcome)
    }

    override suspend fun deleteFavoriteToDB(welcome: Welcome) {
        localSource.deleteFavorite(welcome)
    }

    override suspend fun insertCurrentToDB(welcome: Welcome) {
        localSource.insertCurrentHome(welcome)
    }

    override suspend fun getCurrentWeatherFromDBNoConnection(): Welcome?{
        return localSource.getCurrentWeatherFromDB()
    }

    override suspend fun getAlerts(): Flow<List<Alert>> {
        return localSource.getAllAlerts()
    }

    override suspend fun insertAlertToDB(alert: Alert) {
        localSource.insertAlert(alert)
    }

    override suspend fun deleteAlertFromDB(alert: Alert) {
        localSource.deleteAlert(alert)
    }

}