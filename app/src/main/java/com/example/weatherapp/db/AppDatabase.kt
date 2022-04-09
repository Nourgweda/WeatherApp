package com.example.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Weather

@Database(entities = [FavWeather::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase ?{
            if (instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    "FavoriteWeather"
                ).fallbackToDestructiveMigration().build()
            }
//            else{
//                println("hello")
//            }
            return instance
        }
    }

}