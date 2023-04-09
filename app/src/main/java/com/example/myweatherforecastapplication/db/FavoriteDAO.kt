package com.example.myweatherforecastapplication.db

import android.util.Log
import androidx.room.*
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Weather
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {
    @Query("SELECT *FROM weather WHERE state = :state")
    fun getAllFavorites(state: String): Flow<List<Welcome>>

    @Query("SELECT * From weather WHERE state = :state")
    suspend fun getCurrent(state: String): List<Welcome>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(welcome: Welcome)

    @Update
    suspend fun updateFavorite(welcome: Welcome)

    @Delete
    suspend fun deleteFavorite(welcome: Welcome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrent(welcome: Welcome)

    suspend fun insertOrUpdate(welcome: Welcome) {
        val itemFromDB = getCurrent("current")
        if (!itemFromDB.isEmpty()) {
            if (itemFromDB.get(0).state == "current") {
                deleteFavorite(itemFromDB.get(0))
                insertCurrent(welcome)
            } else
                insertCurrent(welcome)
        }

    }
}
