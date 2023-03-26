package com.example.myweatherforecastapplication.adapters

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

class HourlyWeatherAdapter(var context: Context): ListAdapter<Current, HourlyWeatherAdapter.ProductViewHolder>(MyDiffUtil()) {

    class ProductViewHolder (private val itemView: View): RecyclerView.ViewHolder(itemView){
        val weatherIcon: ImageView
            get() =itemView.findViewById(R.id.icon_of_weather_for_one_item)

        val weatherTime: TextView
            get()= itemView.findViewById(R.id.time_for_one_item)

        val weatherDegree: TextView
            get() =itemView.findViewById(R.id.degree_for_one_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.design_for_one_item_for_all_tempreture_for_one_day,parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val simpleDate = SimpleDateFormat("hh:mm")
        val currentHourlyWeather:Current =getItem(position)
        val currentDate = simpleDate.format(currentHourlyWeather.dt)
        holder.weatherDegree.text= currentHourlyWeather.temp.toString()
        holder.weatherTime.text=currentDate
//        Glide.with(context).load(currentHourlyWeather.weather.get(position).icon).into(holder.weatherIcon)


    }

}
