package com.example.myweatherforecastapplication.favorite.view

import com.example.myweatherforecastapplication.model.Favorite
interface OnClickListener {
    fun favDetails(favorite: Favorite)
    fun addFavToDB(favorite: Favorite)
    fun removeFavFromDB(favorite: Favorite)
}