package com.example.myweatherforecastapplication.settingScreen.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.PreferenceHelper.notification
import com.example.myweatherforecastapplication.PreferenceHelper.temperatureUnit
import com.example.myweatherforecastapplication.PreferenceHelper.userLocation
import com.example.myweatherforecastapplication.PreferenceHelper.windSpeedUnit
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME

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
        saveSettingButton.setOnClickListener { savedSettingsToSharedPref() }


        return view
    }

    private fun savedSettingsToSharedPref() {
        prefs.userLocation = selectedLocation
        prefs.temperatureUnit = selectedTempUnit
        prefs.windSpeedUnit = selectedWindSpeedUnit
        prefs.language = selectedLanguage
        prefs.notification = selectedNotification
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