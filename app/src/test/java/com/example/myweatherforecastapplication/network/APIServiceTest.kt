package com.example.myweatherforecastapplication.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class APIServiceTest {

    lateinit var api:APIClient

    @Test
    fun initialize() {
        api=APIClient.getInstance()
    }

    @Test
    fun getCurrentWeather_latAndLon_() =runBlockingTest{
        //send lat and lon
        val lat=33.4
        val lon=-94.04
        //when call getCurrentWeather
//        val result=api.getCurrentWeather(lat,lon)

    }
}