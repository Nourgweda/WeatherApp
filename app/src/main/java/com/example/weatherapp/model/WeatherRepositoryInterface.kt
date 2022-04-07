package com.example.weatherapp.model

import retrofit2.Response

interface WeatherRepositoryInterface {
    suspend fun getWeather(latitude:Double,longitude:Double) : Response<Weather>
}