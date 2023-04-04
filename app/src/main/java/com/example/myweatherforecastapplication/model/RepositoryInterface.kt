package com.example.myweatherforecastapplication.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeather(lat:Double,lon:Double):Welcome
    suspend fun getFavoritesDB(): Flow<List<Favorite>>
    suspend fun addFavoriteToDB(favorite: Favorite)
    suspend fun updateFavoriteToDB(favorite: Favorite)
    suspend fun deleteFavoriteToDB(favorite: Favorite)
}