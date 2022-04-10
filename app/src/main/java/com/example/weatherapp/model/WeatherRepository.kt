package com.example.weatherapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.db.LocaleSource
import com.example.weatherapp.network.RemoteSource
import retrofit2.Response

class WeatherRepository(
    var remoteSource: RemoteSource,
    var localeSource: LocaleSource,
    var context: Context
) : WeatherRepositoryInterface{

    companion object {
        private var instance: WeatherRepository? = null
        fun getRepoInstance(
            remoteSource: RemoteSource,
            localeSource: LocaleSource,
            context: Context
        ): WeatherRepository {
            Log.d("TAG", "getRepoInstance: weatherrepository instance ")
            return instance ?: WeatherRepository(remoteSource, localeSource,context)
        }
    }

    override suspend fun getWeather(latitude:Double,longitude:Double): Response<Weather> = remoteSource.getWeather(latitude,longitude)
    override  fun insertWeather(favWeather: FavWeather) {
        localeSource.insert(favWeather)
    }
    override  fun deletetWeather(favWeather: FavWeather) {
        localeSource.delete(favWeather)
    }
    override fun getFavoriteWeather(): LiveData<List<FavWeather>> =localeSource.getFavoriteWeather()

    override fun insertAlarm(alarm: Alarm){
        localeSource.insert(alarm)
    }

    override fun getAlarm(): LiveData<List<Alarm>> =localeSource.getAllAlarm()

    override fun deleteAlarm(alarm: Alarm) {
        localeSource.delete(alarm)
    }
}