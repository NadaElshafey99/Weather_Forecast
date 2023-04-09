package com.example.myweatherforecastapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Welcome

@Database(entities = [Welcome::class], version = 2)
@TypeConverters(TypeConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java, "favoriteDB"
                ).build()
                instance = inst
                inst
            }

        }
    }
}