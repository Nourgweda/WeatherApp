package com.example.weatherapp.settings.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.WeatherRepository

//class SettingsViewModelFactory(private val weatherRepository: WeatherRepository, private val context: Context):
//    ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
//            SettingsViewModel(this.weatherRepository,this.context) as T
//        } else {
//            throw IllegalArgumentException("ViewModel Not Found")
//        }
//    }
//}