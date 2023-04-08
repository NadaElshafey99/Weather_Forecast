package com.example.myweatherforecastapplication.homeScreen.viewmodel

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
    lon: Double
) : ViewModel() {

    private var weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var weather: LiveData<Welcome> = weatherLiveData

    private var favWeatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var favWeather: LiveData<Welcome> = favWeatherLiveData
    init {
        getCurrentWeather(lat, lon)
    }

    private fun getCurrentWeather(lat: Double?, lon: Double?) {

        viewModelScope.launch(Dispatchers.IO)
        {
            weatherLiveData.postValue(repository.getWeather(lat?:0.0, lon?:0.0))

        }
    }
     private fun getFavWeather(lat: Double?, lon: Double?) {

        viewModelScope.launch(Dispatchers.IO)
        {
            favWeatherLiveData.postValue(repository.getWeather(lat?:0.0, lon?:0.0))

        }
    }

}