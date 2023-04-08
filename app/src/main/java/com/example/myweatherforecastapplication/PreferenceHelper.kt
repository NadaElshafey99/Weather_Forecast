package com.example.myweatherforecastapplication

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    val USER_LOCATION = "USER_LOCATION"
    val USER_NOTIFICATION = "NOTIFICATION"
    val USER_LONGITUDE = "LONGITUDE"
    val USER_LATITUDE = "LATITUDE"
    val TEMPERATURE_UNIT = "TEMPERATURE_UNIT"
    val WIND_SPEED_UNIT = "WIND_SPEED_UNIT"
    val LANGUAGE = "LANGUAGE"
    val UNIT = "UNIT"
    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userLocation
        get() = getString(USER_LOCATION, "")
        set(value) {
            editMe {
                it.putString(USER_LOCATION, value)
            }
        }

    var SharedPreferences.notification
        get() = getString(USER_NOTIFICATION, "")
        set(value) {
            editMe {
                it.putString(USER_NOTIFICATION, value)
            }
        }
    var SharedPreferences.currentLongitude
        get() = getString(USER_LONGITUDE, "")
        set(value) {
            editMe {
                it.putString(USER_LONGITUDE, value)
            }
        }
    var SharedPreferences.currentLatitude
        get() = getString(USER_LATITUDE, "")
        set(value) {
            editMe {
                it.putString(USER_LATITUDE, value)
            }
        }

    var SharedPreferences.temperatureUnit
        get() = getString(TEMPERATURE_UNIT, "standard")
        set(value) {
            editMe {
                it.putString(TEMPERATURE_UNIT, value)
            }
        }
    var SharedPreferences.windSpeedUnit
        get() = getString(WIND_SPEED_UNIT, "standard")
        set(value) {
            editMe {
                it.putString(WIND_SPEED_UNIT, value)
            }
        }
    var SharedPreferences.language
        get() = getString(LANGUAGE, "en")
        set(value) {
            editMe {
                it.putString(LANGUAGE, value)
            }
        }
    var SharedPreferences.unit
        get() = getString(UNIT, "standard")
        set(value) {
            editMe {
                it.putString(UNIT, value)
            }
        }

}