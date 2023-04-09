package com.example.myweatherforecastapplication.adapters.FavAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Welcome

class MyDiffUtil : DiffUtil.ItemCallback<Welcome>() {
    override fun areItemsTheSame(oldItem: Welcome, newItem: Welcome): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Welcome, newItem: Welcome): Boolean {
        return oldItem == newItem
    }

}