package com.example.myweatherforecastapplication.db

import android.content.Context
import android.util.Log
import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Weather
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.flow.Flow
import kotlin.math.log

class LocalSource private constructor(context: Context) : LocalSourceInterface {
    private val favoriteDAO: FavoriteDAO by lazy {
        val dp: AppDataBase = AppDataBase.getInstance(context)
        dp.favoriteDAO()
    }
    private val alertDAO: AlertDAO by lazy {
        val dp: AppDataBase = AppDataBase.getInstance(context)
        dp.alertDAO()
    }

    companion object {
        private var instance: LocalSource? = null
        fun getInstance(context: Context): LocalSource {
            return instance ?: synchronized(this)
            {
                val temp = LocalSource(context)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getAllFavorites(): Flow<List<Welcome>> {
        return favoriteDAO.getAllFavorites(state = "fav")
    }

    override suspend fun insertFavorite(welcome: Welcome) {
        favoriteDAO.insertFavorite(welcome)
    }

    override suspend fun updateFavorite(welcome: Welcome) {
        favoriteDAO.updateFavorite(welcome)
    }

    override suspend fun deleteFavorite(welcome: Welcome) {
        favoriteDAO.deleteFavorite(welcome)
    }

    override suspend fun insertCurrentHome(welcome: Welcome) {
        favoriteDAO.insertOrUpdate(welcome)
    }

    override suspend fun getCurrentWeatherFromDB(): Welcome? {
        var returnValue: Welcome?
        try {
            returnValue = favoriteDAO.getCurrent("current").get(0)
        } catch (ex: Exception) {
            returnValue = null
        }
        return returnValue
    }

    override suspend fun getAllAlerts(): Flow<List<Alert>> {
        return alertDAO.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        alertDAO.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertDAO.deleteAlert(alert)
    }

}
