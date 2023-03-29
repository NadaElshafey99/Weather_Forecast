package com.example.myweatherforecastapplication.homeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherforecastapplication.model.RepositoryInterface

class HomeScreenViewModelFactory(private val repository: RepositoryInterface,private val lat:Double?,private val lon: Double?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            HomeScreenViewModel(repository,lat?:0.0,lon?:0.0) as T
        } else {
            throw IllegalArgumentException("viewModel class isn't found")
        }
    }
}