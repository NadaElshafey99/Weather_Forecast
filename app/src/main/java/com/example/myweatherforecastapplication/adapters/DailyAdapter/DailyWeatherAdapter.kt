package com.example.myweatherforecastapplication.adapters.DailyAdapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Daily
import com.example.myweatherforecastapplication.model.Icon
import java.text.SimpleDateFormat
import java.util.*

class DailyWeatherAdapter(var context: Context) :
    ListAdapter<Daily, DailyWeatherAdapter.DailyViewHolder>(
        MyDiffUtil()
    ) {

    class DailyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherIcon: ImageView
            get() = itemView.findViewById(R.id.image_of_one_item)

        val weatherDay: TextView
            get() = itemView.findViewById(R.id.date_of_one_item)

        val weatherMinDegree: TextView
            get() = itemView.findViewById(R.id.min_degree_of_one_item)

        val weatherMaxDegree: TextView
            get() = itemView.findViewById(R.id.max_degree_of_one_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.design_for_one_item_for_all_tempreture_for_week,
            parent,
            false
        )
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val prefs = PreferenceHelper.customPreference(context, CUSTOM_PREF_NAME)
        val simpleDate = SimpleDateFormat("EE, dd", Locale(prefs.language ?: "en"))
        val currentDailyWeather: Daily = getItem(position)
        val currentDate = simpleDate.format(currentDailyWeather.dt * 1000L)
        holder.weatherDay.text = currentDate
        holder.weatherMaxDegree.text = "${currentDailyWeather.temp.max}°"
        holder.weatherMinDegree.text = "${currentDailyWeather.temp.min}°"
        val icon = currentDailyWeather.weather.get(0).icon.lowercase()
        val imageResource: Int =
            context.resources.getIdentifier(
                Icon.getIcon(icon),
                "drawable",
                context.packageName
            )
        holder.weatherIcon.setImageResource(imageResource)

    }

}
