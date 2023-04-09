package com.example.myweatherforecastapplication.model

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeather(lat: Double, lon: Double, context: Context): Welcome
    suspend fun getFavoritesDB(): Flow<List<Welcome>>
    suspend fun addFavoriteToDB(welcome: Welcome)
    suspend fun updateFavoriteToDB(welcome: Welcome)
    suspend fun deleteFavoriteToDB(welcome: Welcome)
    suspend fun insertCurrentToDB(welcome: Welcome)
    suspend fun getCurrentWeatherFromDBNoConnection():Welcome
    suspend fun getAlerts():Flow<List<Alert>>
//    suspend fun getOneAlertFromDB():Flow<Alert>
    suspend fun insertAlertToDB(alert:Alert)
    suspend fun deleteAlertFromDB(alert: Alert)


}