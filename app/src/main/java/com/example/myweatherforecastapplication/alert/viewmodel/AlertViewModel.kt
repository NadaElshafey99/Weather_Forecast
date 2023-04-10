package com.example.myweatherforecastapplication.alert.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertViewModel(
    private val repository: RepositoryInterface,
) : ViewModel() {
    private var _alerts: MutableStateFlow<List<Alert>> = MutableStateFlow(emptyList())
    val alerts: MutableStateFlow<List<Alert>> = _alerts

    init {
        getAlerts()
    }

    private fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAlerts().collect {
                _alerts.value = it
            }
        }
    }

    fun insertAlertToDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlertToDB(alert)
            getAlerts()
        }

    }

    fun deleteAlertFromDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlertFromDB(alert)
            getAlerts()
        }

    }

}