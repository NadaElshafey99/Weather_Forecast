package com.example.myweatherforecastapplication.splashScreen.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.example.myweatherforecastapplication.MainActivity
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.USER_LOCATION
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLongitude
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.customPreference
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.notification
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.userLocation
import com.example.myweatherforecastapplication.splashScreen.viewmodel.SplashScreenViewModel
import com.google.android.gms.location.*
import java.util.*

const val PERMISSION_LOCATION_ID = 44

class SplashScreen : AppCompatActivity() {
    val CUSTOM_PREF_NAME = "settings"
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var locationViewModel: SplashScreenViewModel
    private lateinit var topTitle: TextView
    private lateinit var morocco: TextView
    private lateinit var largeDegree: TextView
    private lateinit var northAfrica: TextView
    private lateinit var timeOne: TextView
    private lateinit var timeTwo: TextView
    private lateinit var degreeOne: TextView
    private lateinit var degreeTwo: TextView
    private lateinit var sunImage: ImageView
    private lateinit var stormImage: ImageView
    private lateinit var dialog: Dialog
    private lateinit var okButton: Button
    private lateinit var notificationON: Switch
    private lateinit var radioGroupChoiceLocation: RadioGroup
    private lateinit var radioButtonOfSelectedLocation: RadioButton
    lateinit var myFusedLocationClient: FusedLocationProviderClient
    private lateinit var notification: String
    private lateinit var location: String
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        dialog = Dialog(this)
        myFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initializeUI()
        lottieAnimationView.animate().translationY(-1600F).setDuration(1000).startDelay = 4000
        topTitle.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        morocco.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        largeDegree.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        northAfrica.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        timeOne.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        timeTwo.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        degreeOne.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        degreeTwo.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        sunImage.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        stormImage.animate().translationY(-2000F).setDuration(1000).startDelay = 4000
        prefs = customPreference(this, CUSTOM_PREF_NAME)


        Handler().postDelayed({
            showDialog()
        }, 5000)
    }

    private fun initializeUI() {
        lottieAnimationView = findViewById(R.id.lottie_SplashScreen)
        topTitle = findViewById(R.id.top_titleOfSplashScreen)
        morocco = findViewById(R.id.morocco)
        largeDegree = findViewById(R.id.large_degree)
        northAfrica = findViewById(R.id.northAfrica)
        timeOne = findViewById(R.id.time1)
        timeTwo = findViewById(R.id.time2)
        degreeOne = findViewById(R.id.degree1)
        degreeTwo = findViewById(R.id.degree2)
        sunImage = findViewById(R.id.sunImage)
        stormImage = findViewById(R.id.stromImage)
        dialog.setContentView(R.layout.popup_dialog)
        okButton = dialog.findViewById(R.id.ok_button)
        notificationON = dialog.findViewById(R.id.on_offNotification)
        radioGroupChoiceLocation = dialog.findViewById(R.id.locationChoice)
    }
    private fun showDialog() {
        notification = "ON"
        location = ""
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        if (!prefs.contains(USER_LOCATION)) {
            dialog.show()
            radioGroupChoiceLocation.setOnCheckedChangeListener { group, checkedId ->
                radioButtonOfSelectedLocation = dialog.findViewById(checkedId)
                location = radioButtonOfSelectedLocation.text.toString()
                Toast.makeText(this@SplashScreen, location, Toast.LENGTH_SHORT).show()
            }
            notificationON.setOnCheckedChangeListener { _, isChecked ->
                notification = if (isChecked) "ON" else "OFF"
                Toast.makeText(this@SplashScreen, notification, Toast.LENGTH_SHORT).show()
            }
            okButton.setOnClickListener {
                prefs.userLocation = location
                prefs.notification = notification
                navigateToHomeScreen()

            }
        } else {
            location = prefs.userLocation.toString()
            notification = prefs.notification.toString()
            navigateToHomeScreen()
        }
    }
    private fun navigateToHomeScreen() {
        if (location == "GPS")
            getLastLocation()

        val intent = Intent(this@SplashScreen, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        myFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationDate()
            } else {
                Toast.makeText(this, R.string.pleaseTurnONYourLocation, Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun requestNewLocationDate() {
        locationViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)
        locationViewModel.getLocationLiveData()
            .observe(this, Observer {
                prefs.currentLatitude = it.latitude
                prefs.currentLongitude = it.longitude
            })
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_LOCATION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val result =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        return result
    }


}
