package com.example.weatherapp.alerts.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Alarm
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private val weatherRepository: WeatherRepository, private val context: Context): ViewModel() {

    var alarmList = weatherRepository.getAlarm()
    lateinit var listAl:LiveData<List<Alarm>>


     fun insertAlarm(alarm: Alarm){
         CoroutineScope(Dispatchers.IO).launch {
             weatherRepository.insertAlarm(alarm)
         }
    }


    fun deleteAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.deleteAlarm(alarm)
        }
    }

    fun getAlarm(): LiveData<List<Alarm>> {
        CoroutineScope(Dispatchers.IO).launch {
            listAl=weatherRepository.getAlarm()
        }
        return listAl
    }
}