package com.example.weatherapp.home.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.WeatherClient
import com.example.weatherapp.network.WeatherService
import com.example.weatherapp.preferences.Preference
import kotlinx.coroutines.*
import retrofit2.Response

class OnHourViewModel (private val weatherRepository: WeatherRepository,private var context: Context) : ViewModel() {


    lateinit var preference: Preference

    val errorMessage = MutableLiveData<String>()
    val weatherList = MutableLiveData<Weather>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    //try shared preference
    lateinit var sharedPreferences: SharedPreferences
   // lateinit var context: Context

    init {
        getWeather(context)
    }

    val liveDataWeather : LiveData<Weather> = weatherList

     fun getWeather(context: Context) {

         //try
         preference= Preference.getInstance(context)!!
         //sharedPreferences= context.getSharedPreferences("PREF",Context.MODE_PRIVATE)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = weatherRepository.getWeather(preference.getPreference("LATITUDE")!!
                .toDouble(),preference.getPreference("LONGITUDE")!!.toDouble())

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TAG", "getWeather: VIEMODEL DATA" + response.body())
                    weatherList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

//    fun dataReceivedFromHome(longi:Double,lati:Double){
//        longituteSend=longi
//        Log.d("TAG", "dataReceivedFromHome: "+ longi)
//        Log.d("TAG", "dataReceivedFromHome: "+ longituteSend)
//        latitudeSend=lati
//        Log.d("TAG", "dataReceivedFromHome: "+ lati)
//        Log.d("TAG", "dataReceivedFromHome: "+ latitudeSend)
//    }

}




