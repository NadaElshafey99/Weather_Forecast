package com.example.myweatherforecastapplication.adapters.AlertAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class AlertAdapter(var context: Context) :
    ListAdapter<Alert, AlertAdapter.AlertViewHolder>(
        MyDiffUtil()
    ) {

    class AlertViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fromDate: TextView
            get() = itemView.findViewById(R.id.fromDate)

        val endDate: TextView
            get() = itemView.findViewById(R.id.toDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.design_item_for_alert,
            parent,
            false
        )
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val prefs = PreferenceHelper.customPreference(context, CUSTOM_PREF_NAME)
        val simpleDate = SimpleDateFormat("hh:mm aa, dd MMM", Locale(prefs.language ?: "en"))
        val alertItem: Alert = getItem(position)
        val start = simpleDate.format(alertItem.start * 1000L)
        holder.fromDate.text = start
        val end = simpleDate.format(alertItem.end * 1000L)
        holder.endDate.text = end
    }

}