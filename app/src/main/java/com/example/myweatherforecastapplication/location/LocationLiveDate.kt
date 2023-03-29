package com.example.myweatherforecastapplication.location

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveDate(application: Application) : LiveData<LocationDetails>() {

    var myFusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    override fun onInactive() {
        super.onInactive()
        myFusedLocationClient.removeLocationUpdates(myLocationCallback)
    }

    override fun onActive() {
        super.onActive()
        requestNewLocationDate()
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationDate() {
        val myLocationRequest = LocationRequest()
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        myLocationRequest.setInterval(0)
        myFusedLocationClient.requestLocationUpdates(
            myLocationRequest,
            myLocationCallback,
            Looper.myLooper()
        )
    }
    private val myLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val myLastLocation: Location? = locationResult.lastLocation
            value= LocationDetails(myLastLocation?.longitude.toString(),myLastLocation?.latitude.toString())

        }
    }
}