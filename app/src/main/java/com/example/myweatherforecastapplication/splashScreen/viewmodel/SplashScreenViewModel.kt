package com.example.myweatherforecastapplication.splashScreen.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myweatherforecastapplication.location.LocationLiveDate

class SplashScreenViewModel(application: Application) : AndroidViewModel(application){
    private val locationLiveData= LocationLiveDate(application)
    fun getLocationLiveData()=locationLiveData
}