package com.example.weatherapp.home.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.db.ConcreteLocalSource
import com.example.weatherapp.db.LocaleSource
import com.example.weatherapp.home.viewmodel.OnHourViewModel
import com.example.weatherapp.home.viewmodel.OnHourViewModelFactory
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.RemoteSource
import com.example.weatherapp.network.WeatherClient
import com.example.weatherapp.network.WeatherService
import com.example.weatherapp.preferences.Preference
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnHourClickListener {

    var receivedLatitude: Double? = null
    var receivedLongitude: Double? = null


    //hour
    lateinit var recyclerViewHour: RecyclerView
    lateinit var hourViewModel: OnHourViewModel
    lateinit var weHourAdapter: WeHourAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var onHourViewModelFactory: OnHourViewModelFactory
    lateinit var localeSource: LocaleSource


    //day
    lateinit var recyclerViewDay: RecyclerView
    lateinit var weDayAdapter: WeDayAdapter
    lateinit var layoutManagerDay: LinearLayoutManager

    //current
    lateinit var locationCurrentTV: TextView
    lateinit var dateCurrentTV: TextView
    lateinit var descriptionCurrentTV: TextView
    lateinit var tempCurrentTV: TextView
    lateinit var iconCurrentImg: ImageView
    var locationCurrentStr: String? = null
    var dateCurrentStr: String? = null
     var descriptionCurrentStr: String? = null
     var tempCurrentStr: String? = null
     var iconCurrentStr: String? = null

    lateinit var actualPressureTV: TextView
    lateinit var actualHumidityTV: TextView
    lateinit var actualWindTV: TextView
    lateinit var actualCloudTV: TextView
    lateinit var actualUltraVioletTV: TextView
    lateinit var actualVisibilityTV: TextView


    //send data
    lateinit var preference: Preference
    //lateinit var sharedPreferences: SharedPreferences

    var  la : Double=0.0
        var lo:Double = 0.0


    //private val currentWeatherArrayList: ArrayList<Weather> = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hour

    }

    private fun setUpRetrofit() {
        hourViewModel.liveDataWeather.observe(this) {
            Log.d("TAG", "setUpRetrofit: SUCCESS" + it)
            if (it != null) {
                Log.d("TAG", "setUpRetrofit:here$it")
                weHourAdapter.setDataHour(it.hourly)
                weDayAdapter.setDataDay(it.daily)
                locationCurrentStr = it.timezone
                Log.d("TAG", "setUpRetrofit: LOCATION"+locationCurrentStr)
                val currentDt:Double= it.current.dt
                dateCurrentStr=convertToData(currentDt)
                Log.d("TAG", "setUpRetrofit: LOCATION"+dateCurrentStr)
                descriptionCurrentStr=it.current.weather[0].description
                Log.d("TAG", "setUpRetrofit: LOCATION"+descriptionCurrentStr)
                tempCurrentStr=it.current.temp.toString()
                Log.d("TAG", "setUpRetrofit: LOCATION"+tempCurrentStr)
                iconCurrentStr = "https://openweathermap.org/img/wn/${it.current.weather[0].icon}@2x.png"

                //setting with views
                locationCurrentTV.text =locationCurrentStr
                dateCurrentTV.text=dateCurrentStr
                descriptionCurrentTV.text=descriptionCurrentStr
                tempCurrentTV.text=tempCurrentStr
                Glide.with(this).load(iconCurrentStr).into(iconCurrentImg)

                actualPressureTV.text = it.current.pressure.toString()
                actualHumidityTV.text = it.current.humidity.toString()+" %"
                actualWindTV.text=it.current.windSpeed.toString()+" m/s"
                actualCloudTV.text=it.current.clouds.toString()+" %"
                actualUltraVioletTV.text=it.current.uvi.toString()
                actualVisibilityTV.text=it.current.visibility.toString()




            }
        }
        hourViewModel.errorMessage.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            Log.d("TAG", "setUpRetrofit: ERROR" + it)

        }

        hourViewModel.loading.observe(this, Observer {
            if (it) {
            } else {
            }
        })





    }

    private fun setUpRecyclerView() {
        //hour
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        weHourAdapter = WeHourAdapter(context!!, ArrayList())
        recyclerViewHour.adapter = weHourAdapter
        recyclerViewHour.layoutManager = layoutManager

        //day
        layoutManagerDay= LinearLayoutManager(requireContext())
        layoutManagerDay.orientation=RecyclerView.VERTICAL
        weDayAdapter= WeDayAdapter(context!!,ArrayList())
        recyclerViewDay.adapter=weDayAdapter
        recyclerViewDay.layoutManager=layoutManagerDay



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val bundle = arguments
        val lati = bundle?.getDouble("latitude")
        val longi = bundle?.getDouble("longitude")
        receivedLatitude = lati
        receivedLongitude = longi
        Log.d("TAG", "onCreate: LATITUE in maps " + receivedLatitude)
        Log.d("TAG", "onCreate: LONGITUDE in maps" + receivedLongitude)

        //try shared perederences here

//        la= receivedLatitude!!
//        lo=receivedLongitude!!


        Log.d("TAG", "onCreateView: TRANSFORMED"+receivedLatitude)
        Log.d("TAG", "onCreateView: TRANSFORMED"+receivedLongitude)


        if(receivedLongitude!=null && receivedLatitude!=null) {
//            val receivedLatitudeLong: Long = receivedLatitude!!.toLong()
//            val receivedLongitudeLong: Long = receivedLongitude!!.toLong()
//            Log.d("TAG", "onCreateView: 3AAAAAAAAA " + receivedLatitudeLong)
//            Log.d("TAG", "onCreateView: 3AAAAAAAAA " + receivedLongitudeLong)
//            sharedPreferences= context!!.getSharedPreferences("PREF",Context.MODE_PRIVATE)
//            var editor = sharedPreferences.edit()
//            editor.putLong("LATITUDE",receivedLatitudeLong)
//            editor.putLong("LONGITUDE",receivedLongitudeLong)
//            editor.apply()
            preference= Preference.getInstance(context!!.applicationContext)!!
            preference.sendPreference("LATITUDE",receivedLatitude!!.toLong())
            preference.sendPreference("LONGITUDE",receivedLongitude!!.toLong())



           // another trial
//            val lon: Double = 61.5240
//            val lat :Double = 105.3188
//            preference.sendPreference("LATITUDE",lon!!.toLong())
//            preference.sendPreference("LONGITUDE",lat!!.toLong())

        }




//        preference= Preference.getInstance(context!!)!!
//        preference.sendPreference("LATITUDE",receivedLatitude?.toLong())
//        preference.sendPreference("LONGITUDE",receivedLongitude?.toLong())

//        val long = receivedLatitude?.toLong()
//        Log.d("TAG", "onCreate: CONVERTED LONG" + receivedLatitude)


        //2 text views
//        val latitueTv:TextView=view.findViewById(R.id.latitudeTV)
//        val longituteTv:TextView=view.findViewById(R.id.longituteTV)
//         latiStr  = receivedLatitude.toString()
//         longStr = receivedLongitude.toString()
//        latitueTv.setText(latiStr)
//        longituteTv.setText(longStr)

//        weatherClient = WeatherClient.getClientInstance()!!
//        weatherClient.dataReceivedFromHome(90.0000,45.0000)






        //try to retreive data
        locationCurrentTV=view.findViewById(R.id.locationCurrentTV)
        dateCurrentTV=view.findViewById(R.id.dateCurrentTV)
        descriptionCurrentTV=view.findViewById(R.id.descriptionCurrentTV)
        tempCurrentTV=view.findViewById(R.id.tempCurrentTV)
        iconCurrentImg=view.findViewById(R.id.iconCurrentImg)

        actualPressureTV=view.findViewById(R.id.actualPressureTV)
        actualHumidityTV=view.findViewById(R.id.actualHumidityTV)
        actualWindTV=view.findViewById(R.id.actualWindTV)
        actualCloudTV=view.findViewById(R.id.actualCloudTV)
        actualUltraVioletTV=view.findViewById(R.id.actualUltraVioletTV)
        actualVisibilityTV=view.findViewById(R.id.actualVisibilityTV)

//        locationCurrentTV.text =locationCurrentStr
//        dateCurrentTV.text=dateCurrentStr
//        descriptionCurrentTV.text=descriptionCurrentStr
//        tempCurrentTV.text=tempCurrentStr
//        Glide.with(this).load(iconCurrentStr).into(iconCurrentImg)



        //day
        recyclerViewDay=view.findViewById(R.id.recyclerViewDay)
        //hour
        recyclerViewHour = view.findViewById(R.id.recyclerViewHour)
        onHourViewModelFactory = OnHourViewModelFactory(
            WeatherRepository.getRepoInstance(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(this.requireContext()),
                this.context!!
            ),this.context!!
        )
        hourViewModel =
            ViewModelProvider(this, onHourViewModelFactory).get(OnHourViewModel::class.java)
//        hourViewModel.dataReceivedFromHome(receivedLatitude!!,receivedLongitude!!)
//        Log.d("TAG", "onCreateView: FROM HOME = "+receivedLatitude)
//        Log.d("TAG", "onCreateView: FROM HOME = "+receivedLongitude)

        setUpRecyclerView()
        //hourViewModel.getWeather()
        setUpRetrofit()






        return view
    }

    override fun onClick(weather: Weather) {
//        hourViewModel.
//        Toast.makeText(this,"Movie added",Toast.LENGTH_SHORT).show()
    }

//    fun setDataHour(currentList: List<Weather>) {
//        this.currentWeatherArrayList.apply {
//            clear()
//            addAll(currentList)
//            //notifyDataSetChanged()
//        }
//
//    }

    private fun convertToData(dt:Double):String{
        val date = Date((dt*1000).toLong())
        val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        return format.format(date)
    }




}