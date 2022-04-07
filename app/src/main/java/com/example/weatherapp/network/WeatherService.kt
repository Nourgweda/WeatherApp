package com.example.weatherapp.network

import com.example.weatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appId:String="cb25c33a06ba3c9d1a92f7e08d670e7f",
        @Query("units") unit:String="metric",
        @Query("lang") lang:String="en"
    ): Response<Weather>



}