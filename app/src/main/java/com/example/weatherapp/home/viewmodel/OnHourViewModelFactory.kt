package com.example.weatherapp.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.WeatherRepository

class OnHourViewModelFactory (private val weatherrepository: WeatherRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OnHourViewModel::class.java)) {
            OnHourViewModel(this.weatherrepository,context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}