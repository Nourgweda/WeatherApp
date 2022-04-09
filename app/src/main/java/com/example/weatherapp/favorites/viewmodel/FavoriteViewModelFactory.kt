package com.example.weatherapp.favorites.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.WeatherRepository


class FavoriteViewModelFactory (private val weatherRepository: WeatherRepository,private val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(this.weatherRepository,this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}