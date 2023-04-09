package com.example.myweatherforecastapplication.db

import androidx.room.TypeConverter
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.Daily
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun fromStringToCurrent(value: String): Current = Gson().fromJson(value, Current::class.java)

    @TypeConverter
    fun fromCurrentToString(current: Current) = Gson().toJson(current)

    @TypeConverter
    fun fromJsonToDailyList(value: String) =
        Gson().fromJson(value, Array<Daily>::class.java).toList()

    @TypeConverter
    fun fromDailyListToJson(list: List<Daily>) = Gson().toJson(list)

    @TypeConverter
    fun fromJsonToHourlyList(value: String) =
        Gson().fromJson(value, Array<Current>::class.java).toList()

    @TypeConverter
    fun fromHourlyListToJson(list: List<Current>) = Gson().toJson(list)
}