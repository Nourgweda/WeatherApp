package com.example.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.model.FavWeather

@Dao
interface WeatherDao {

    @Insert
     fun insert(favWeather: FavWeather)
    @Delete
     fun delete(favWeather: FavWeather)
    @Query("SELECT * from FavoriteWeather")
    fun getAllWeather() : LiveData<List<FavWeather>>


}
