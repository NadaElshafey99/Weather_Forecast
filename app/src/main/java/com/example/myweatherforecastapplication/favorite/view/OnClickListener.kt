package com.example.myweatherforecastapplication.favorite.view

import com.example.myweatherforecastapplication.model.Welcome

interface OnClickListener {
    fun favDetails(welcome: Welcome)
    fun addFavToDB(welcome: Welcome)
    fun removeFavFromDB(welcome: Welcome)
}