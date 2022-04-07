package com.example.weatherapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.weatherapp.network.RemoteSource
import retrofit2.Response

class WeatherRepository(
    var remoteSource: RemoteSource,
    var context: Context
) : WeatherRepositoryInterface{

    companion object {
        private var instance: WeatherRepository? = null
        fun getRepoInstance(
            remoteSource: RemoteSource,
            context: Context
        ): WeatherRepository {
            Log.d("TAG", "getRepoInstance: weatherrepository instance ")
            return instance ?: WeatherRepository(remoteSource, context)
        }
    }

    override suspend fun getWeather(latitude:Double,longitude:Double): Response<Weather> = remoteSource.getWeather(latitude,longitude)




}