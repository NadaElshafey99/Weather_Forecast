package com.example.myweatherforecastapplication.adapters.AlertAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherforecastapplication.model.Alert

class MyDiffUtil : DiffUtil.ItemCallback<Alert>() {
    override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem == newItem
    }

}