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
import com.example.myweatherforecastapplication.model.Icon
import java.text.SimpleDateFormat
import java.util.*

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
        val simpleDate = SimpleDateFormat("hh aa")
        val currentHourlyWeather: Current = getItem(position)
        val currentDate = simpleDate.format(currentHourlyWeather.dt * 1000L)
        holder.weatherDegree.text = "${currentHourlyWeather.temp}°"
        holder.weatherTime.text = currentDate.uppercase()
        val icon = currentHourlyWeather.weather.get(0).icon.lowercase()
        val imageResource: Int =
            context.resources.getIdentifier(
                Icon.getIcon(icon),
                "drawable",
                context.packageName
            )
        holder.weatherIcon.setImageResource(imageResource)

    }

}
