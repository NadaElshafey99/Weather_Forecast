package com.example.myweatherforecastapplication.settingScreen.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.PreferenceHelper.notification
import com.example.myweatherforecastapplication.PreferenceHelper.temperatureUnit
import com.example.myweatherforecastapplication.PreferenceHelper.unit
import com.example.myweatherforecastapplication.PreferenceHelper.userLocation
import com.example.myweatherforecastapplication.PreferenceHelper.windSpeedUnit
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.location.locationmap.MapsLocationDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingScreen : Fragment() {
    private lateinit var radioGroupForLocation: RadioGroup
    private lateinit var radioButtonForLocation: RadioButton
    private lateinit var radioGroupForTemperature: RadioGroup
    private lateinit var radioButtonForTemperature: RadioButton
    private lateinit var radioGroupForWindSpeed: RadioGroup
    private lateinit var radioButtonForWindSpeed: RadioButton
    private lateinit var radioGroupForLanguage: RadioGroup
    private lateinit var radioButtonForLanguage: RadioButton
    private lateinit var radioGroupForNotification: RadioGroup
    private lateinit var radioButtonForNotification: RadioButton
    private lateinit var saveSettingButton: Button
    private lateinit var selectedLocation: String
    private lateinit var selectedTempUnit: String
    private lateinit var selectedWindSpeedUnit: String
    private lateinit var selectedLanguage: String
    private lateinit var selectedNotification: String
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
        selectedLocation = getString(R.string.gps)
        selectedTempUnit = getString(R.string.celsius)
        selectedWindSpeedUnit = getString(R.string.meterPerSec)
        selectedLanguage = getString(R.string.arabic)
        selectedNotification = getString(R.string.enable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting_screen, container, false)
        saveSettingButton = view.findViewById(R.id.save_button)
        radioGroupForLocation = view.findViewById(R.id.radioGroupForLocation)
        radioGroupForTemperature = view.findViewById(R.id.radioGroupForTemperature)
        radioGroupForWindSpeed = view.findViewById(R.id.radioGroupForWindSpeed)
        radioGroupForLanguage = view.findViewById(R.id.radioGroupForLanguage)
        radioGroupForNotification = view.findViewById(R.id.radioGroupForNotification)
        setRadioAction(view)
        saveSettingButton.setOnClickListener {
            showDialog(view)

        }


        return view
    }

    private fun showDialog(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.sureToSave)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                savedSettingsToSharedPref(view)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun savedSettingsToSharedPref(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {

            prefs.windSpeedUnit = selectedWindSpeedUnit
            prefs.temperatureUnit = selectedTempUnit
            when (selectedLocation == getString(R.string.map)) {
                true -> prefs.userLocation = "MAP"
                false -> prefs.userLocation = "GPS"
            }
            when (selectedTempUnit) {
                getString(R.string.celsius) -> prefs.unit = "metric"
                getString(R.string.kelvin) -> prefs.unit = "standard"
                getString(R.string.fahrenheit) -> prefs.unit = "imperial"
            }
            when (selectedWindSpeedUnit) {
                getString(R.string.meterPerSec) -> prefs.unit = "metric"
                getString(R.string.milePerHour) -> prefs.unit = "imperial"
            }

            when (selectedLanguage == getString(R.string.arabic)) {
                true -> prefs.language = "ar"
                false -> prefs.language = "en"
            }
            when (selectedNotification == getString(R.string.enable)) {
                true -> prefs.notification = "ENABLE"
                false -> prefs.notification = "DISABLE"
            }
            withContext(Dispatchers.Main)
            {
                delay(1000)
                when (prefs.userLocation == "GPS") {
                    true -> Navigation.findNavController(view)
                        .navigate(R.id.navigateFromSettingScreenToHomeScreen)
                    false -> {
                        val action =
                            SettingScreenDirections.navigateFromSettingScreenToMapsLocation(
                                fromDestination = "setting"
                            )
                        Navigation.findNavController(view).navigate(action)
                    }
                }


            }
        }
    }

    private fun setRadioAction(view: View) {
        radioGroupForLocation.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForLocation = view.findViewById(checkedId)
            selectedLocation = radioButtonForLocation.text as String
        }
        radioGroupForTemperature.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForTemperature = view.findViewById(checkedId)
            selectedTempUnit = radioButtonForTemperature.text as String
        }
        radioGroupForWindSpeed.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForWindSpeed = view.findViewById(checkedId)
            selectedWindSpeedUnit = radioButtonForWindSpeed.text as String
        }
        radioGroupForLanguage.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForLanguage = view.findViewById(checkedId)
            selectedLanguage = radioButtonForLanguage.text as String
        }
        radioGroupForNotification.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForNotification = view.findViewById(checkedId)
            selectedNotification = radioButtonForNotification.text as String
        }
    }
}