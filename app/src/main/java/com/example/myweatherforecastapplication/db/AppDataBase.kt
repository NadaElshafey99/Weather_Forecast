package com.example.myweatherforecastapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.model.Welcome

@Database(entities = [Welcome::class, Alert::class], version = 3)
@TypeConverters(TypeConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO
    abstract fun alertDAO(): AlertDAO
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