package com.example.myweatherforecastapplication.adapters.DailyAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherforecastapplication.model.Daily


class MyDiffUtil: DiffUtil.ItemCallback<Daily>()
{
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem===newItem
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem==newItem
    }

}