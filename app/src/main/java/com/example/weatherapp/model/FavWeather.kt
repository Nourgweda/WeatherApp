package com.example.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteWeather")
data class FavWeather(


    @ColumnInfo(name = "lattitude")
    @NonNull
     var lattitude:Double,

    @ColumnInfo(name = "longitude")
    @NonNull
     var longitude:Double,

    @ColumnInfo(name = "location")
    @NonNull
     var location:String,

    @PrimaryKey
    @ColumnInfo(name = "ID")
    @NonNull
    var id:String
)
//{
//    @PrimaryKey(autoGenerate = true)
//      var id: Int? = 0
//}

    //@PrimaryKey(autoGenerate = true) val id: Int?

    //@PrimaryKey(autoGenerate = true)private val id: Int? = 0
