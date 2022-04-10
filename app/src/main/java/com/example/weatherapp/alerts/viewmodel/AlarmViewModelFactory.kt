package com.example.weatherapp.alerts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModel
import com.example.weatherapp.model.WeatherRepository

class AlarmViewModelFactory (private val weatherRepository: WeatherRepository, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            AlarmViewModel(this.weatherRepository,this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}