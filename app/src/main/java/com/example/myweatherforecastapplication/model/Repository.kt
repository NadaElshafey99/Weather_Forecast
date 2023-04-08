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

    override suspend fun getFavoritesDB(): Flow<List<Favorite>> {
        return localSource.getAllFavorites()
    }

    override suspend fun addFavoriteToDB(favorite: Favorite) {
        localSource.insertFavorite(favorite)
    }

    override suspend fun updateFavoriteToDB(favorite: Favorite) {
        localSource.updateFavorite(favorite)
    }

    override suspend fun deleteFavoriteToDB(favorite: Favorite) {
        localSource.deleteFavorite(favorite)
    }
}