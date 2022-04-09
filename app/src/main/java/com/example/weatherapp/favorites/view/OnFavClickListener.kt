package com.example.weatherapp.favorites.view

import com.example.weatherapp.model.FavWeather


interface OnFavClickListener {
    fun onClick(favWeather: FavWeather)
}