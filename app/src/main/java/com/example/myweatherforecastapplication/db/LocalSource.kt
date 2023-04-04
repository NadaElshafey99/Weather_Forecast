package com.example.myweatherforecastapplication.db

import android.content.Context
import com.example.myweatherforecastapplication.model.Favorite
import kotlinx.coroutines.flow.Flow

class LocalSource private constructor(context: Context) : LocalSourceInterface {
    private val favoriteDAO: FavoriteDAO by lazy {
        val dp: AppDataBase = AppDataBase.getInstance(context)
        dp.favoriteDAO()
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
    override suspend fun getAllFavorites(): Flow<List<Favorite>> {
        return favoriteDAO.getAllFavorites()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        favoriteDAO.insertFavorite(favorite)
    }

    override suspend fun updateFavorite(favorite: Favorite) {
        favoriteDAO.updateFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        favoriteDAO.deleteFavorite(favorite)
    }
}