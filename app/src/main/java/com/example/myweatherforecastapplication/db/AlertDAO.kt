package com.example.myweatherforecastapplication.db

import androidx.room.*
import com.example.myweatherforecastapplication.model.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {

    @Query("SELECT *FROM alert")
    fun  getAllAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert)//:Long return id

    @Delete
    suspend fun deleteAlert(alert:Alert)
}