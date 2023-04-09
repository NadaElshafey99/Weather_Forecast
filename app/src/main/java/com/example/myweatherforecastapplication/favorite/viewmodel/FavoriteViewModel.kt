package com.example.myweatherforecastapplication.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.RepositoryInterface
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: RepositoryInterface,
) : ViewModel() {
    private var _favorites: MutableLiveData<List<Welcome>> = MutableLiveData()
    val favorites: LiveData<List<Welcome>> = _favorites
    private var _weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    val weatherOfSelectedCountry: LiveData<Welcome> = _weatherLiveData

    init {
        getFavorites()
    }
    private fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritesDB().collect {
                _favorites.postValue(it)
            }
        }
    }
    fun getWeatherOfSelectedFav(lat: Double?, lon: Double?, context: Context) {

        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO)
            {
                _weatherLiveData.postValue(repository.getWeather(lat ?: 0.0, lon ?: 0.0, context))
            }
        }
    }
    fun addProductToDB(welcome: Welcome) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoriteToDB(welcome)
            getFavorites()
        }

    }
    fun deleteProductFromDB(welcome: Welcome) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteToDB(welcome)
            getFavorites()
        }

    }
}