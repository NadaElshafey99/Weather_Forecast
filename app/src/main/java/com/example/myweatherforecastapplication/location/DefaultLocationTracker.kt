/*package com.example.myweatherforecastapplication.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*

const val PERMISSION_LOCATION_ID = 44

class DefaultLocationTracker(
    private var locationProviderClient: FusedLocationProviderClient,
    private var application: Application,
) {

    var myFusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
//    getLastLocation()

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationDate()
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
           applicationContext,
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
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_LOCATION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    private val myLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val myLastLocation: Location? = p0.lastLocation
//            longitude.text = myLastLocation?.longitude.toString()
//            latitude.text = myLastLocation?.latitude.toString()
//            val addressList = geocoder.getFromLocation(
//                myLastLocation?.latitude ?: 0.0, myLastLocation?.longitude ?: 0.0, 1
//            )
//            if ((addressList != null && addressList.size > 0)) {
//                val address = addressList.get(0)
//                addressText.text =" ${address.locality} \n ${address.countryName}"
//            }
        }
    }
}

*/