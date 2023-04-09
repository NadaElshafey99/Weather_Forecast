package com.example.myweatherforecastapplication.alert.view


import com.example.myweatherforecastapplication.model.Alert

interface OnClickListener {
    fun addAlertToDB(alert: Alert)
    fun removeAlertFromDB(alert: Alert)
}