package com.example.myweatherforecastapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    val lat: Double,
    val lon: Double,
    val temp: Double?,
    val icon: String?
)