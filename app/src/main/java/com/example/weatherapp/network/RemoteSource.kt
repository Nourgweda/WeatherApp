package com.example.weatherapp.network


import com.example.weatherapp.model.Weather
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeather(latitude:Double,longitude:Double,unit:String,language:String): Response<Weather>
}