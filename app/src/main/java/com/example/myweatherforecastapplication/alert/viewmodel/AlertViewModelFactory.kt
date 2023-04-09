package com.example.myweatherforecastapplication.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherforecastapplication.model.RepositoryInterface

class AlertViewModelFactory (private val repository: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(repository) as T
        } else {
            throw IllegalArgumentException("viewModel class isn't found")
        }
    }
}