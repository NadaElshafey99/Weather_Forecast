package com.example.myweatherforecastapplication.adapters.HourlyAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.Weather

class MyDiffUtil: DiffUtil.ItemCallback<Current>()
{
    override fun areItemsTheSame(oldItem: Current, newItem: Current): Boolean {
        return oldItem===newItem
    }

    override fun areContentsTheSame(oldItem: Current, newItem: Current): Boolean {
        return oldItem==newItem
    }

}