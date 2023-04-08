package com.example.myweatherforecastapplication.location.locationmap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.myweatherforecastapplication.MainActivity
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.databinding.MapsLocationBinding
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.PreferenceHelper.currentLongitude
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException

class MapsLocation : Fragment(), OnMapReadyCallback {

    private lateinit var binding: MapsLocationBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    val CUSTOM_PREF_NAME = "settings"
    private lateinit var selectedLocation: Button
    private lateinit var markerOptions: MarkerOptions
    private var longitude = 0.0
    private var latitude = 0.0
    private lateinit var destination: String
    private val mapsArgs: MapsLocationArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapsLocationBinding.inflate(inflater, container, false)
        selectedLocation = binding.doneButton
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        destination = arguments?.getString("previousDestination").toString()
        Log.i("TAG", "onCreateView: $destination")
        Log.i("TAG", "Lat: $latitude")
        Log.i("TAG", "Long: $longitude")
        mapInitialize()
        selectedLocation.setOnClickListener {
            when (destination == "initial") {
                true -> {
                    destination = "fav"
                    val prefs =
                        PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
                    prefs.currentLatitude = latitude.toString()
                    prefs.currentLongitude = longitude.toString()
                    val intent = Intent(context, MainActivity::class.java)
                    context?.startActivity(intent)
                    activity?.finish()
                }
                false -> {
                    try {
                        val args = mapsArgs.fromDestination
                        when (args == "setting") {
                            true -> {
                                val action =
                                    MapsLocationDirections.navigateFromMapsLocationToHomeScreen(
                                        "$latitude", "$longitude", "setting"
                                    )
                                Navigation.findNavController(it).navigate(action)
                            }
                            false -> {
                                val action =
                                    MapsLocationDirections.navigateFromMapsLocationToFavorite(
                                        "$latitude",
                                        "$longitude"
                                    )
                                Navigation.findNavController(it).navigate(action)
                            }
                        }
                    } catch (ex: Exception) {
                        print(ex.printStackTrace())
                    }

                }
            }
            Log.i("TAG", "Lat: $latitude")
            Log.i("TAG", "Long: $longitude")

        }
        return binding.root

    }

    private fun mapInitialize() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.smallestDisplacement = 16F
        locationRequest.fastestInterval = 3000

        binding.searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event?.action == KeyEvent.ACTION_DOWN
                || event?.action == KeyEvent.KEYCODE_ENTER
            ) {
                goToSearchLocation()
            }

            false
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

    }

    private fun goToSearchLocation() {
        val searchLocation = binding.searchBar.text.toString()
        val geocoder = Geocoder(requireContext())
        var address: List<Address> = listOf()
        try {
            address = geocoder.getFromLocationName(searchLocation, 1) as List<Address>
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (address.isNotEmpty()) {
            val addressResult = address.get(0)
            val location = addressResult.adminArea
            longitude = addressResult.longitude
            latitude = addressResult.latitude
            gotoLatLng(latitude, longitude, 17F)
            if (marker != null)
                marker?.remove()
            markerOptions = MarkerOptions()
            markerOptions.title(location)
            markerOptions.icon(
                BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
            markerOptions.position(LatLng(latitude, longitude))
            marker = googleMap?.addMarker(markerOptions)


        }

    }

    private fun gotoLatLng(latitude: Double, longitude: Double, zoom: Float) {
        val latLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
//        this.latitude = latitude
//        this.longitude = longitude
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(cameraUpdate)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        return

                    googleMap.isMyLocationEnabled = true
                    if (googleMap != null) {
                        googleMap.setOnMyLocationChangeListener { location ->
                            googleMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    )
                                ).title("it's me")
                            )
                            latitude = location.latitude
                            longitude = location.longitude
                            Log.i("TAG", "onPermissionGranted: $latitude $longitude")
                        };
                    }
                    fusedLocationProviderClient.lastLocation.addOnFailureListener {
                        Toast.makeText(context, "error ${it.message}", Toast.LENGTH_LONG).show()
                    }.addOnSuccessListener {
                        val latLng = LatLng(it.latitude, it.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17F))
                    }

                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {
                    Toast.makeText(
                        context,
                        "permission ${permissionDeniedResponse?.permissionName} asd denied",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }
            }).check()
    }
}