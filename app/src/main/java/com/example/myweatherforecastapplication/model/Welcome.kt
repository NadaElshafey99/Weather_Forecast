package com.example.myweatherforecastapplication.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Welcome (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    var state:String?,
    val lat: Double?,
    val lon: Double?,
    var timezone: String?,
    val current: Current?,
    val hourly: List<Current?>,
    val daily: List<Daily?>,
)