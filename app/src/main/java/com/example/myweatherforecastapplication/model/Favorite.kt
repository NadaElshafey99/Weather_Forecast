package com.example.myweatherforecastapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    val id:Int,
    val state:String,
    val lat: Double,
    val lon: Double,
    val timeZone:String?,
    val temp: Double?,
    val icon: String?
)