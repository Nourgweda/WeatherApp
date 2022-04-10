package com.example.weatherapp.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.Alarm
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Weather

class ConcreteLocalSource (var context: Context) : LocaleSource {

    private val dao: WeatherDao
    var FavoriteList : MutableLiveData<List<FavWeather>> = MutableLiveData()
    init {
        val db: AppDatabase? = AppDatabase.getInstance(context)
        dao  = db!!.weatherDao()
    }
    override fun getFavoriteWeather(): LiveData<List<FavWeather>> = dao.getAllWeather()

    override  fun delete(favWeather: FavWeather) {
        dao.delete(favWeather)
    }



    override  fun insert(favWeather: FavWeather) {
        dao.insert(favWeather)
    }

    override fun insert(alarm: Alarm){
        dao.insert(alarm)
    }

    override fun getAllAlarm(): LiveData<List<Alarm>> =dao.getAllAlarm()

    override fun delete(alarm: Alarm) {
        dao.delete(alarm)
    }

}