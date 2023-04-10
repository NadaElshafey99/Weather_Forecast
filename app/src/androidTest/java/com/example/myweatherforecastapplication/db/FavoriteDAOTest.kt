package com.example.myweatherforecastapplication.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherforecastapplication.model.Current
import com.example.myweatherforecastapplication.model.Weather
import org.hamcrest.core.Is.`is`
import com.example.myweatherforecastapplication.model.Welcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteDAOTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var database: AppDataBase
    private lateinit var favoriteDAO: FavoriteDAO

    @Before
    fun initializeDB() {
        //initialize DB
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build() //to remove data after testing
        favoriteDAO = database.favoriteDAO()

    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllFavorites_fav_returnAllFavOrNullForNoItem() = runBlockingTest {
        //given->insert object of welcome to DB
        val favItem = Welcome(
            0,
            "fav",
            31.2071394,
            29.9262512,
            "Asia/Dubai",
            null,
            emptyList(), emptyList()
        )
        favoriteDAO.insertFavorite(favItem)
        val currentItem = Welcome(
            2,
            "current",
            31.2071394,
            29.9262512,
            "Asia/Dubai",
            null,
            emptyList(), emptyList()
        )
        favoriteDAO.insertFavorite(currentItem)

        //when->call getAllFavorites
        val result = favoriteDAO.getAllFavorites("fav").first()

        //then-> fav=currentItem
        assertThat(result, `is`(favItem))
    }

    @Test
    fun getCurrent_returnHomeItemOrNullForNoItem() = runBlockingTest {
        //given->insert object of welcome to DB
        val favItem = Welcome(
            id = 0,
            state = "current",
            lat = 31.2071394,
            lon = 29.9262512,
            timezone = "Asia / Dubai",
            current = Current(
                dt = 1681128000,
                sunrise = null,
                sunset = null,
                temp = 27.79,
                feels_like = 28.14,
                pressure = 1010,
                humidity = 49,
                dewPoint = 0.0,
                uvi = 2.68,
                clouds = 14,
                visibility = 10000,
                wind_speed = 4.59,
                windDeg = 0,
                weather = listOf( Weather(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
                rain = null,
                windGust = null
            ),
            hourly = listOf(),
            daily = listOf()
        )
        favoriteDAO.insertFavorite(favItem)

        val currentItem = Welcome(
            0,
            "current",
            31.2071394,
            29.9262512,
            "Asia/Dubai",
            Current(
                dt = 1681128000,
                sunrise = null,
                sunset = null,
                temp = 27.79,
                feels_like = 28.14,
                pressure = 1010,
                humidity = 49,
                dewPoint = 0.0,
                uvi = 2.68,
                clouds = 14,
                visibility = 10000,
                wind_speed = 4.59,
                windDeg = 0,
                weather = listOf(
                    Weather(
                        id = 801,
                        main = "Clouds",
                        description = "few clouds",
                        icon = "02d"
                    )
                ),
                rain = null,
                windGust = null
            ),
            emptyList(), emptyList()
        )
        favoriteDAO.insertFavorite(currentItem)

        //when->call getAllFavorites
        val result = favoriteDAO.getCurrent("current").first()

        //then-> fav=currentItem
        assertThat(result, `is`(currentItem))
    }

    @Test
    fun insertFavorite_welcomeObject() = runBlockingTest {
        //given->insert object of welcome to DB
        val favItem = Welcome(
            0,
            "fav",
            31.2071394,
            29.9262512,
            "Asia/Dubai",
            Current(
                dt = 1681128000,
                sunrise = null,
                sunset = null,
                temp = 27.79,
                feels_like = 28.14,
                pressure = 1010,
                humidity = 49,
                dewPoint = 0.0,
                uvi = 2.68,
                clouds = 14,
                visibility = 10000,
                wind_speed = 4.59,
                windDeg = 0,
                weather = listOf(
                    Weather(
                        id = 801,
                        main = "Clouds",
                        description = "few clouds",
                        icon = "02d"
                    )
                ),
                rain = null,
                windGust = null
            ),
            emptyList(), emptyList()
        )

        //when->call insert
        favoriteDAO.insertFavorite(favItem)

        //Then
        val res = favoriteDAO.insertFavorite(favItem)
        assertThat(res, `is`(Matchers.nullValue()))
    }

}