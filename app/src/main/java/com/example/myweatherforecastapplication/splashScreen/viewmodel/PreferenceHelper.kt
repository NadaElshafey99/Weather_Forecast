package com.example.myweatherforecastapplication.splashScreen.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    val USER_LOCATION = "USER_LOCATION"
    val USER_NOTIFICATION = "NOTIFICATION"

    fun defaultPreference(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

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

   /* var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }*/
}