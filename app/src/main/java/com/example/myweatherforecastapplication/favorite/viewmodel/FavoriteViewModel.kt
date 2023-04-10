package com.example.myweatherforecastapplication.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class FavoriteViewModel(
    private val repository: RepositoryInterface,
) : ViewModel() {
    private var _favorites: MutableStateFlow<List<Welcome>> = MutableStateFlow(emptyList())
    val favorites: MutableStateFlow<List<Welcome>> = _favorites
    private var _weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    val weatherOfSelectedCountry: LiveData<Welcome> = _weatherLiveData
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    init {
        getFavorites()
    }
    private fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            repository.getFavoritesDB().collect {
                _favorites.value=it
            }
        }
    }
    fun getWeatherOfSelectedFav(lat: Double?, lon: Double?, context: Context) {

        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler)
            {
                _weatherLiveData.postValue(repository.getWeather(lat ?: 0.0, lon ?: 0.0, context))
            }
        }
    }
    fun addProductToDB(welcome: Welcome) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            repository.addFavoriteToDB(welcome)
            getFavorites()
        }

    }
    fun deleteProductFromDB(welcome: Welcome) {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            repository.deleteFavoriteToDB(welcome)
            getFavorites()
        }

    }
}