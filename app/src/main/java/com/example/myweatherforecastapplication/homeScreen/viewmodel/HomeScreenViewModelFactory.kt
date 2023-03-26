package com.example.myweatherforecastapplication.homeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherforecastapplication.model.RepositoryInterface

class HomeScreenViewModelFactory(private val repository: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            HomeScreenViewModel(repository) as T
        } else {
            throw IllegalArgumentException("viewModel class isn't found")
        }
    }
}