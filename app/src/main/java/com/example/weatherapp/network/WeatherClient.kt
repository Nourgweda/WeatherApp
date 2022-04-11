package com.example.weatherapp.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bumptech.glide.Glide.init
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.home.view.HomeFragment
import com.example.weatherapp.model.Weather
import com.example.weatherapp.preferences.Preference
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient private constructor(): RemoteSource  {


        var longituteSend: Double = 0.0
        var latitudeSend: Double =0.0

    //lateinit var homeFragment:HomeFragment

    //lateinit var homeActivity: HomeActivity

    //try to get data
    lateinit var preference: Preference
//    lateinit var context: Context
//    lateinit var sharedPreferences: SharedPreferences

    companion object {
        private var instance: WeatherClient? = null
        fun getClientInstance(): WeatherClient? {
            return instance?: WeatherClient()
        }
    }

    object RetrofitHelper {
        val baseUrl = "https://api.openweathermap.org/data/2.5/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                // we need to add converter factory to
                // convert JSON object to Java object
                .build()
        }
    }

//    fun dataReceivedFromHome(longi:Double,lati:Double){
//        longituteSend=longi
//        Log.d("TAG", "dataReceivedFromHome: "+ longi)
//        Log.d("TAG", "dataReceivedFromHome: "+ longituteSend)
//        latitudeSend=lati
//        Log.d("TAG", "dataReceivedFromHome: "+ lati)
//        Log.d("TAG", "dataReceivedFromHome: "+ latitudeSend)
//    }



    override suspend fun getWeather(latitude:Double,longitude:Double,unit:String,lang:String): Response<Weather>
    {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)


//        Log.d("TAG", "dataReceivedFromHome: INSIDE METHOD"+ longituteSend)
//        Log.d("TAG", "dataReceivedFromHome: INSDIDE METHOD"+ latitudeSend)
        return weatherService.getWeather(latitude,longitude,unit = unit,lang = lang)
    }





}