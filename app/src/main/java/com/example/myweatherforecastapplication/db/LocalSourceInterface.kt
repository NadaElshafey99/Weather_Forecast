package com.example.myweatherforecastapplication.db

import com.example.myweatherforecastapplication.model.Favorite
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {
    suspend fun getAllFavorites():Flow<List<Favorite>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun updateFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
}