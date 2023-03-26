package com.example.myweatherforecastapplication.homeScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.adapters.HourlyWeatherAdapter
import com.example.myweatherforecastapplication.homeScreen.viewmodel.HomeScreenViewModel
import com.example.myweatherforecastapplication.homeScreen.viewmodel.HomeScreenViewModelFactory
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.Repository
import com.example.myweatherforecastapplication.network.APIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat

class HomeScreen : Fragment() {

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var countryName: TextView
    private lateinit var countryDegree: TextView
    private lateinit var currentDay: TextView
    private lateinit var currentDescription:TextView
    private lateinit var iconDescribeWeather:ImageView
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private var hourlyList: MutableList<Current> ?= mutableListOf()
    private lateinit var simpleDate: SimpleDateFormat
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var homeScreenViewModelFactory: HomeScreenViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        countryName = view.findViewById(R.id.country_name)
        countryDegree = view.findViewById(R.id.current_degree_of_country)
        currentDay = view.findViewById(R.id.date)
        currentDescription=view.findViewById(R.id.description_for_current_day)
        iconDescribeWeather=view.findViewById(R.id.icon_describe_weather)
        simpleDate= SimpleDateFormat("EEEE dd")
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext())
        homeScreenViewModelFactory =
            HomeScreenViewModelFactory(Repository.getInstance(APIClient.getInstance()))
        homeScreenViewModel =
            ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)
        lifecycleScope.launch(Dispatchers.IO)
        {
//            homeScreenViewModel.getHourlyWeather()
            try {
                homeScreenViewModel.getCurrentWeather()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_LONG).show()

            }
            withContext(Dispatchers.Main)
            {

                homeScreenViewModel.weather.observe(requireActivity()) { weather ->
                    if (weather != null) {
                        countryName.text = weather.timezone
                        countryDegree.text = "${weather.current.temp} °C"
                        currentDay.text = simpleDate.format(weather.current.dt*1000L)
                        currentDescription.text=weather.current.weather.get(0).description
                        hourlyList=weather.hourly as MutableList<Current>
                        hourlyWeatherAdapter.submitList(hourlyList)
                        hourlyRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context).apply {
                                orientation = RecyclerView.HORIZONTAL
                                adapter = hourlyWeatherAdapter
                            }
                        }
//                        Glide.with(requireContext()).load("https://openweathermap.org/img/wn/${weather.current.weather.get(0).icon}@2x.png").into(iconDescribeWeather)
                    } else {
                        Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_LONG).show()
                    }
                }
                /*hourlyWeatherAdapter.submitList(hourlyList)
                hourlyRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context).apply {
                        orientation = RecyclerView.HORIZONTAL
                        adapter = hourlyWeatherAdapter
                    }
                }*/
            }
        }
        return view
    }
}