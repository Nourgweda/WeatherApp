package com.example.weatherapp.db

import androidx.lifecycle.LiveData
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Weather

interface LocaleSource {
    fun getFavoriteWeather() : LiveData<List<FavWeather>>
     fun delete(favWeather: FavWeather)
     fun insert(favWeather: FavWeather)
}