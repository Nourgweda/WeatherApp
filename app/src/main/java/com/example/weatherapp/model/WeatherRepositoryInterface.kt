package com.example.weatherapp.model

import androidx.lifecycle.LiveData
import retrofit2.Response

interface WeatherRepositoryInterface {
    suspend fun getWeather(latitude:Double,longitude:Double) : Response<Weather>
     fun insertWeather(favWeather: FavWeather)
     fun deletetWeather(favWeather: FavWeather)
    fun getFavoriteWeather() : LiveData<List<FavWeather>>
}