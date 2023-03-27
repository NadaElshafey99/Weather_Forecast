package com.example.myweatherforecastapplication.adapters.HourlyAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.model.Current
import java.text.SimpleDateFormat

class HourlyWeatherAdapter(var context: Context) :
    ListAdapter<Current, HourlyWeatherAdapter.HourlyViewHolder>(
        MyDiffUtil()
    ) {

    class HourlyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherIcon: ImageView
            get() = itemView.findViewById(R.id.icon_of_weather_for_one_item)

        val weatherTime: TextView
            get() = itemView.findViewById(R.id.time_for_one_item)

        val weatherDegree: TextView
            get() = itemView.findViewById(R.id.degree_for_one_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.design_for_one_item_for_all_tempreture_for_one_day,
            parent,
            false
        )
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        repeat(24)
        {
            val simpleDate = SimpleDateFormat("hh aa")
            val currentHourlyWeather: Current = getItem(position)
            val currentDate = simpleDate.format(currentHourlyWeather.dt * 1000L)
            holder.weatherDegree.text = currentHourlyWeather.temp.toString()
            holder.weatherTime.text = currentDate.uppercase()
            var icon = currentHourlyWeather.weather.get(0).icon.lowercase()
            when {
                icon.startsWith("01") -> {
                    icon = "clearsky"
                }
                icon.startsWith("02") -> {
                    icon = "fewclouds"
                }
                icon.startsWith("03") -> {
                    icon = "scatteredclouds"
                }
                icon.startsWith("04") -> {
                    icon = "brokenclouds"
                }
                icon.startsWith("09") -> {
                    icon = "showerrain"
                }
                icon.startsWith("10") -> {
                    icon = "rain"
                }
                icon.startsWith("11") -> {
                    icon = "thunderstorm"
                }
                icon.startsWith("13") -> {
                    icon = "snow"
                }
                icon.startsWith("50") -> {
                    icon = "mist"
                }
            }
            val imageResource: Int =
                context.getResources().getIdentifier(icon, "drawable", context.getPackageName())
            holder.weatherIcon.setImageResource(imageResource)
        }


    }

}
