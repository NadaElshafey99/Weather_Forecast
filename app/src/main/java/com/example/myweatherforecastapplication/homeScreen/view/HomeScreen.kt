package com.example.myweatherforecastapplication.homeScreen.view

import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.PreferenceHelper.currentLongitude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>
    private lateinit var prefs: SharedPreferences
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val homeArgs: HomeScreenArgs by navArgs()
    private var fromDestination: String = "fav"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleDate = SimpleDateFormat("EEEE , dd")
        simpleSunrise = SimpleDateFormat("hh:mm aa")
        prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext())
        dailyWeatherAdapter = DailyWeatherAdapter(requireContext())
//        prefs.itemDisplay="current"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        initialUI(view)
        try {
            latitude = homeArgs.lat?.toDouble()
            longitude = homeArgs.lon?.toDouble()
//            if (prefs.itemDisplay=="current") {
//                latitude = prefs.currentLatitude?.toDouble()
//                longitude = prefs.currentLongitude?.toDouble()
//                fromDestination=homeArgs.fromDestination.toString()
//            }

        } catch (ex: Exception) {
            latitude = prefs.currentLatitude?.toDouble()
            longitude = prefs.currentLongitude?.toDouble()
            print(ex.printStackTrace())
        }
        homeScreenViewModelFactory =
            HomeScreenViewModelFactory(
                Repository.getInstance(
                    APIClient.getInstance(),
                    LocalSource.getInstance(requireContext())
                ),
                latitude,
                longitude
            )
        homeScreenViewModel =
            ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)

        homeScreenViewModel.weather.observe(requireActivity()) { weather ->
            if (weather != null) {
                lifecycleScope.launch(Dispatchers.Main){
                    updateUI(weather)
                }

                Log.i("TAG", "Latitude: ${weather.lat}")
                Log.i("TAG", "Longitude: ${weather.lon}")
            } else {
                Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        Log.i("TAG", "onPause: ")

    }

    override fun onStop() {
        super.onStop()
        Log.i("TAG", "onStop: ")
        homeArgs.toBundle().clear()
//        findNavController().popBackStack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("TAG", "onDestroyView: ")
//        prefs.itemDisplay="current"
//        findNavController().popBackStack()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("TAG", "onSaveInstanceState: ")
        super.onSaveInstanceState(outState)
        outState.putString("fromDestination", fromDestination);
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
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        addresses = geocoder.getFromLocation(weather.lat, weather.lon, 1) as List<Address>
        val state = addresses.get(0).getAdminArea()
        val country = addresses.get(0).getCountryName()
        val split = state.split(" ").toTypedArray()
        val timeZone = "${split[0]} \n $country"
        countryName.text = timeZone
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