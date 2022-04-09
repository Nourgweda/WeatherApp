package com.example.weatherapp.favorites.view

import com.example.weatherapp.model.FavWeather


interface OnItemClickListener {
    fun onClick(favWeather: FavWeather)

    fun onItem(favWeather: FavWeather)
}