package com.example.weatherapp.favorites.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val weatherRepository: WeatherRepository, private val context: Context):ViewModel(){

    var favoriteWeatherList = weatherRepository.getFavoriteWeather()
    lateinit var listFav :LiveData<List<FavWeather>>


    fun insertWeather(favWeather: FavWeather) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.insertWeather(favWeather)
        }
    }

    fun deleteWeather(favWeather: FavWeather) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.deletetWeather(favWeather)
        }
    }

    fun getFavoriteWeather(): LiveData<List<FavWeather>> {
        CoroutineScope(Dispatchers.IO).launch {
            listFav=weatherRepository.getFavoriteWeather()
        }
        return listFav
    }


}