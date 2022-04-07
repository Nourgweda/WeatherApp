package com.example.weatherapp.home.view

import com.example.weatherapp.model.Weather

interface OnHourClickListener {
    fun onClick (weather: Weather)
}