package com.example.myweatherforecastapplication.db

import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {
    suspend fun getAllFavorites(): Flow<List<Welcome>>
    suspend fun insertFavorite(welcome: Welcome)
    suspend fun updateFavorite(welcome: Welcome)
    suspend fun deleteFavorite(welcome: Welcome)
    suspend fun insertCurrentHome(welcome: Welcome)
    suspend fun getCurrentWeatherFromDB(): Welcome?
    suspend fun getAllAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

}