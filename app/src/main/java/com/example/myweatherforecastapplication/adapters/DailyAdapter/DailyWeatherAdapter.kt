package com.example.myweatherforecastapplication.adapters.DailyAdapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.model.Daily
import java.text.SimpleDateFormat

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

        val weatherDesc: TextView
            get() = itemView.findViewById(R.id.description_of_one_item)

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
        repeat(7) {
            val simpleDate = SimpleDateFormat("EE, dd")
            val currentDailyWeather: Daily = getItem(position)
            val currentDate = simpleDate.format(currentDailyWeather.dt * 1000L)
            holder.weatherDay.text = currentDate
            holder.weatherMaxDegree.text = currentDailyWeather.temp.max.toString()
            holder.weatherMinDegree.text = currentDailyWeather.temp.min.toString()
            holder.weatherDesc.text = currentDailyWeather.weather.get(0).description
            var icon = currentDailyWeather.weather.get(0).icon.lowercase()
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
