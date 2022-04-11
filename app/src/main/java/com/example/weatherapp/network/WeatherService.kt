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
        @Query("appid") appId:String="07bc32cbc42b16533d7a7d702420bc9a",
        @Query("units") unit:String="metric",
        @Query("lang") lang:String="en"
    ): Response<Weather>



}