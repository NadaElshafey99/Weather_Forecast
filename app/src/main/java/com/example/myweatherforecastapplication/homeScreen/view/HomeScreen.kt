package com.example.myweatherforecastapplication.homeScreen.view

import android.content.SharedPreferences
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
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.adapters.DailyAdapter.DailyWeatherAdapter
import com.example.myweatherforecastapplication.adapters.HourlyAdapter.HourlyWeatherAdapter
import com.example.myweatherforecastapplication.db.LocalSource
import com.example.myweatherforecastapplication.homeScreen.viewmodel.HomeScreenViewModel
import com.example.myweatherforecastapplication.homeScreen.viewmodel.HomeScreenViewModelFactory
import com.example.myweatherforecastapplication.model.*
import com.example.myweatherforecastapplication.network.APIClient
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLongitude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat

val CUSTOM_PREF_NAME = "settings"

class HomeScreen : Fragment() {

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var countryName: TextView
    private lateinit var countryDegree: TextView
    private lateinit var currentDay: TextView
    private lateinit var currentDescription: TextView
    private lateinit var currentPressure: TextView
    private lateinit var currentHumidity: TextView
    private lateinit var currentWind: TextView
    private lateinit var currentCloud: TextView
    private lateinit var currentUV: TextView
    private lateinit var currentVisibility: TextView
    private lateinit var currentSunrise: TextView
    private lateinit var currentSunset: TextView
    private lateinit var currentFeelsLike: TextView
    private lateinit var iconDescribeWeather: ImageView
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    private lateinit var simpleDate: SimpleDateFormat
    private lateinit var simpleSunrise: SimpleDateFormat
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var homeScreenViewModelFactory: HomeScreenViewModelFactory
    private var hourlyList: MutableList<Current>? = mutableListOf()
    private var dailyList: MutableList<Daily>? = mutableListOf()
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleDate = SimpleDateFormat("EEEE , dd")
        simpleSunrise = SimpleDateFormat("hh:mm aa")
        prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext())
        dailyWeatherAdapter = DailyWeatherAdapter(requireContext())
        homeScreenViewModelFactory =
            HomeScreenViewModelFactory(
                Repository.getInstance(
                    APIClient.getInstance(),
                    LocalSource.getInstance(requireContext())
                ),
                prefs.currentLatitude?.toDouble(),
                prefs.currentLongitude?.toDouble()
            )
        homeScreenViewModel =
            ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        initialUI(view)
        /*lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                homeScreenViewModel.getCurrentWeather(
                    prefs.currentLatitude?.toDouble(),
                    prefs.currentLongitude?.toDouble()
                )
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_LONG).show()
            }*/
        //   withContext(Dispatchers.Main)
        // {

        homeScreenViewModel.weather.observe(requireActivity()) { weather ->
            if (weather != null) {
                updateUI(weather)
            } else {
                Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_LONG).show()
            }
        }
        // }
        //  }
        return view
    }

    private fun initialUI(view: View) {
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        dailyRecyclerView = view.findViewById(R.id.dailyRecyclerView)
        countryName = view.findViewById(R.id.country_name)
        countryDegree = view.findViewById(R.id.current_degree_of_country)
        currentDay = view.findViewById(R.id.date)
        currentDescription = view.findViewById(R.id.description_for_current_day)
        iconDescribeWeather = view.findViewById(R.id.icon_describe_weather)
        currentPressure = view.findViewById(R.id.current_pressure)
        currentHumidity = view.findViewById(R.id.current_humidity)
        currentWind = view.findViewById(R.id.current_wind)
        currentCloud = view.findViewById(R.id.current_cloud)
        currentVisibility = view.findViewById(R.id.current_visibility)
        currentUV = view.findViewById(R.id.current_UV)
        currentSunrise = view.findViewById(R.id.current_sunRise)
        currentSunset = view.findViewById(R.id.current_sunSet)
        currentFeelsLike = view.findViewById(R.id.current_feelsLike)
    }

    private fun updateUI(weather: Welcome) {
        countryName.text = weather.timezone
        countryDegree.text = "${weather.current.temp} °"
        currentDay.text = simpleDate.format(weather.current.dt * 1000L)
        currentDescription.text = weather.current.weather.get(0).description
        hourlyList = (weather.hourly).take(24) as MutableList<Current>
        dailyList = weather.daily as MutableList<Daily>
        currentPressure.text = "${weather.current.pressure} hpa"
        currentHumidity.text = "${weather.current.humidity} %"
        currentWind.text = "${weather.current.wind_speed} m/s"
        currentCloud.text = "${weather.current.clouds} %"
        currentVisibility.text = "${weather.current.visibility} m"
        currentUV.text = "${weather.current.uvi}"
        currentSunset.text =
            simpleSunrise.format((weather.current.sunset?.times(1000L) ?: 0))
        currentSunrise.text =
            simpleSunrise.format(weather.current.sunrise?.times(1000L) ?: 0)
        currentFeelsLike.text = weather.current.feels_like.toString()
        hourlyWeatherAdapter.submitList(hourlyList)
        hourlyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
                adapter = hourlyWeatherAdapter
            }
        }
        dailyWeatherAdapter.submitList(dailyList)
        dailyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
                adapter = dailyWeatherAdapter
            }
        }

        val icon = weather.current.weather.get(0).icon.lowercase()
        val imageResource: Int =
            resources.getIdentifier(
                Icon.getIcon(icon),
                "drawable",
                context?.packageName
            )
        iconDescribeWeather.setImageResource(imageResource)
    }
}