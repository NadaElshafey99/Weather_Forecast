package com.example.myweatherforecastapplication.homeScreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Weather
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: RepositoryInterface) : ViewModel() {

    /*private var productsLiveData: MutableLiveData<List<Current>> = MutableLiveData<List<Current>>()
    val products: LiveData<List<Current>> = productsLiveData*/
    private var hourlyLiveData: MutableLiveData<List<Current>> = MutableLiveData<List<Current>>()
    val hourlyWeather: LiveData<List<Current>> = hourlyLiveData
    private lateinit var currentWeather: Welcome
    private lateinit var hourlyWeatherOfDay: Current
    private var productsLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    val weather: LiveData<Welcome> = productsLiveData

    init {
        getCurrentWeather()
    }

   /* private fun getTodayWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            hourlyLiveData.postValue(repository.getHourlyWeather())
        }
    }*/

    /*suspend fun getHourlyWeather(): List<Current> {
        /*viewModelScope.launch(Dispatchers.IO) {
          repository.getHourlyWeather()
        }*/
        return repository.getHourlyWeather()
    }*/

     fun getCurrentWeather(){

        viewModelScope.launch(Dispatchers.IO)
        {
            productsLiveData.postValue(repository.getWeather())

        }
    }
}