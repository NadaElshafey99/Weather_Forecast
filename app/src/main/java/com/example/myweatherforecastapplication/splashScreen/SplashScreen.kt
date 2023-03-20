package com.example.myweatherforecastapplication.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.homeScreen.view.HomeScreen

class SplashScreen : AppCompatActivity() {
    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var topTitle: TextView
    lateinit var morocco: TextView
    lateinit var largeDegree: TextView
    lateinit var northAfrica: TextView
    lateinit var timeOne: TextView
    lateinit var timeTwo: TextView
    lateinit var degreeOne: TextView
    lateinit var degreeTwo: TextView
    lateinit var sunImage: ImageView
    lateinit var stormImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
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

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}