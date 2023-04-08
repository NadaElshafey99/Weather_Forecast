package com.example.myweatherforecastapplication.homeScreen.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val repository: RepositoryInterface,
    lat: Double,
    lon: Double,
    context: Context
) : ViewModel() {

    private var weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var weather: LiveData<Welcome> = weatherLiveData

    init {
        getCurrentWeather(lat, lon,context)
    }

    private fun getCurrentWeather(lat: Double?, lon: Double?,context: Context) {

        viewModelScope.launch(Dispatchers.IO)
        {
            weatherLiveData.postValue(repository.getWeather(lat?:0.0, lon?:0.0,context))

        }
    }

}