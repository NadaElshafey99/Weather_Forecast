package com.example.myweatherforecastapplication.homeScreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val repository: RepositoryInterface,
    val lat: Double,
    val lon: Double
) : ViewModel() {

    private var productsLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    val weather: LiveData<Welcome> = productsLiveData

    init {
        getCurrentWeather(lat, lon)
    }


    fun getCurrentWeather(lat: Double?, lon: Double?) {

        viewModelScope.launch(Dispatchers.IO)
        {
            productsLiveData.postValue(repository.getWeather(lat?:0.0, lon?:0.0))

        }
    }
}