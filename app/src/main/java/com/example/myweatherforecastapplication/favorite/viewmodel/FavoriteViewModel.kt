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
    private var _favorites: MutableLiveData<List<Favorite>> = MutableLiveData()
    val favorites: LiveData<List<Favorite>> = _favorites
    private var weatherLiveData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var weatherOfSelectedCountry: LiveData<Welcome> = weatherLiveData

    init {
        getFavorites()
    }

    fun getWeatherOfSelectedFav(lat: Double?, lon: Double?,context: Context){

        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO)
            {
                weatherLiveData.postValue(repository.getWeather(lat?:0.0, lon?:0.0,context))

            }
        }

    }

    private fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritesDB().collect {
                _favorites.postValue(it)
            }
        }
    }

    fun addProductToDB(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoriteToDB(favorite)
            getFavorites()
        }

    }

    fun deleteProductFromDB(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteToDB(favorite)
            getFavorites()
        }

    }
}