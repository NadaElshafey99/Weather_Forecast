package com.example.myweatherforecastapplication.adapters.FavAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherforecastapplication.model.Favorite

class MyDiffUtil : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }

}