package com.example.myweatherforecastapplication.utils

import com.example.myweatherforecastapplication.model.Welcome

sealed class ViewState{
    object ShowLoading:ViewState()
    class ShowError(val error:Throwable):ViewState()
    class ShowData(val data:Welcome):ViewState()
}
