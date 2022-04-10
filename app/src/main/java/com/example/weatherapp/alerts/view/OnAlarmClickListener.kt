package com.example.weatherapp.alerts.view

import com.example.weatherapp.model.Alarm


interface OnAlarmClickListener {
    fun onClick(alarm: Alarm)
    fun onItem(alarm: Alarm)
}