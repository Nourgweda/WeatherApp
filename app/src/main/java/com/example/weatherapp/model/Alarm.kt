package com.example.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarm")
data class Alarm (
    @ColumnInfo(name = "name")
    @NonNull
    var name:String,

    @ColumnInfo(name = "startDate")
    @NonNull
    var startDate:String,

    @ColumnInfo(name = "endDate")
    @NonNull
    var endDate:String,

    @ColumnInfo(name = "hour")
    @NonNull
    var hour:String,

    @ColumnInfo(name = "minute")
    @NonNull
    var minute:String,

    @ColumnInfo(name = "format")
    @NonNull
    var format:String,

    @PrimaryKey
    @ColumnInfo(name = "ID")
    @NonNull
    var id:String


        )