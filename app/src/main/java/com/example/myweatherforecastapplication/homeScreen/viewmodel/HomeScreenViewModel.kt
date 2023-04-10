package com.example.myweatherforecastapplication.homeScreen.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Welcome
import com.example.myweatherforecastapplication.utils.NetworkConnection
import com.example.myweatherforecastapplication.utils.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val repository: RepositoryInterface,
    val lat: Double,
    val lon: Double,
    val context: Context
) : ViewModel() {

    private var weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var weather: LiveData<Welcome> = weatherLiveData
    private val hasNetworkConnection = NetworkConnection.getInstance(context)

    private var _state: MutableStateFlow<ViewState> =
        MutableStateFlow(ViewState.ShowLoading)
    val state = _state.asSharedFlow()

    init {
        if(hasNetworkConnection.isOnline())
          getCurrentWeather(lat, lon, context)
    }

    //    try {
//        val response = repository.getWeather(lat , lon , context)
//        when (response) {
//            is Throwable -> {
//                _state.value = ViewState.ShowError(response)
//            }
//            else -> {
//                _state.value = ViewState.ShowData(response)
//            }
//        }
//    } catch (ex: Exception) {
//        _state.value = ViewState.ShowError(ex)
//
//    }
    private fun getCurrentWeather(lat: Double?, lon: Double?, context: Context) {
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler)
        {
            val getWeatherFromAPI = launch {
                weatherLiveData.postValue(repository.getWeather(lat ?: 0.0, lon ?: 0.0, context))
            }
            getWeatherFromAPI.join()
            launch {
                saveCurrentWeather()
            }
        }

    }

    private fun saveCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO)
        {
            weatherLiveData.value?.state = "current"
            weatherLiveData.value?.let { repository.insertCurrentToDB(it) }
        }
    }

    suspend fun getCurrentWeatherFromDBNoConnection() =
        repository.getCurrentWeatherFromDBNoConnection()

}