package com.example.myweatherforecastapplication.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "alert")
data class Alert (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    var latitude:Double,
    var longitude:Double,
)