package com.example.myweatherforecastapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class NetworkConnection {
    lateinit var connectionManager: ConnectivityManager
    var connected: Boolean = false

    companion object {
        private val instance: NetworkConnection = NetworkConnection()
        lateinit var context: Context
        fun getInstance(context: Context): NetworkConnection {
            this.context = context.getApplicationContext();
            return instance
        }
    }

    fun isOnline(): Boolean {
        try {
            connectionManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            connected = networkInfo != null
                    && networkInfo.isAvailable()
                    && networkInfo.isConnected();

        } catch (ex: Exception) {
            System.out.println("CheckConnectivity Exception: " + ex.message.toString());
            Log.v("connectivity", ex.toString());
        }
        return connected
    }
}