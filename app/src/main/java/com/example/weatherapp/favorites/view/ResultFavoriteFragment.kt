package com.example.weatherapp.favorites.view

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
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.home.view.WeDayAdapter
import com.example.weatherapp.home.view.WeHourAdapter
import com.example.weatherapp.home.viewmodel.OnHourViewModel
import com.example.weatherapp.home.viewmodel.OnHourViewModelFactory
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.WeatherClient
import com.example.weatherapp.preferences.Preference
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ResultFavoriteFragment : Fragment(), OnFavClickListener {
    //hour
    lateinit var resRecyclerViewHour: RecyclerView
    lateinit var hourViewModel: OnHourViewModel
    lateinit var weHourAdapter: WeHourAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var onHourViewModelFactory: OnHourViewModelFactory
    lateinit var localeSource: LocaleSource
    //day
    lateinit var resRecyclerViewDay: RecyclerView
    lateinit var weDayAdapter: WeDayAdapter
    lateinit var layoutManagerDay: LinearLayoutManager
    //current
    lateinit var resLocationCurrentTV: TextView
    lateinit var resDateCurrentTV: TextView
    lateinit var resDescriptionCurrentTV: TextView
    lateinit var resTempCurrentTV: TextView
    lateinit var resIconCurrentImg: ImageView
    var resLocationCurrentStr: String? = null
    var resDateCurrentStr: String? = null
    var resDescriptionCurrentStr: String? = null
    var resTempCurrentStr: String? = null
    var resIconCurrentStr: String? = null
    lateinit var resActualPressureTV: TextView
    lateinit var resActualHumidityTV: TextView
    lateinit var resActualWindTV: TextView
    lateinit var resActualCloudTV: TextView
    lateinit var resActualUltraVioletTV: TextView
    lateinit var resActualVisibilityTV: TextView
    //send data
    lateinit var resPreference: Preference
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var receivedLocation: LatLng
    var receivedLat: Double? = null
    var receivedLong: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private fun setUpRetrofit() {
        hourViewModel.liveDataWeather.observe(this) {
            Log.d("TAG", "setUpRetrofit: SUCCESS" + it)
            if (it != null) {
                Log.d("TAG", "setUpRetrofit:here$it")
                weHourAdapter.setDataHour(it.hourly)
                weDayAdapter.setDataDay(it.daily)
                resLocationCurrentStr = it.timezone
                Log.d("TAG", "setUpRetrofit: LOCATION" + resLocationCurrentStr)
                val currentDt: Double = it.current.dt
                resDateCurrentStr = convertToData(currentDt)
                Log.d("TAG", "setUpRetrofit: LOCATION" + resDateCurrentStr)
                resDescriptionCurrentStr = it.current.weather[0].description
                Log.d("TAG", "setUpRetrofit: LOCATION" + resDescriptionCurrentStr)
                resTempCurrentStr = it.current.temp.toString()
                Log.d("TAG", "setUpRetrofit: LOCATION" + resTempCurrentStr)
                resIconCurrentStr =
                    "https://openweathermap.org/img/wn/${it.current.weather[0].icon}@2x.png"
                //setting with views
                resLocationCurrentTV.text = resLocationCurrentStr
                resDateCurrentTV.text = resDateCurrentStr
                resDescriptionCurrentTV.text = resDescriptionCurrentStr
                resTempCurrentTV.text = resTempCurrentStr
                Glide.with(this).load(resIconCurrentStr).into(resIconCurrentImg)

                resActualPressureTV.text = it.current.pressure.toString()
                resActualHumidityTV.text = it.current.humidity.toString() + " %"
                resActualWindTV.text = it.current.windSpeed.toString() + " m/s"
                resActualCloudTV.text = it.current.clouds.toString() + " %"
                resActualUltraVioletTV.text = it.current.uvi.toString()
                resActualVisibilityTV.text = it.current.visibility.toString()
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
        resRecyclerViewHour.adapter = weHourAdapter
        resRecyclerViewHour.layoutManager = layoutManager
        //day
        layoutManagerDay= LinearLayoutManager(requireContext())
        layoutManagerDay.orientation=RecyclerView.VERTICAL
        weDayAdapter= WeDayAdapter(context!!,ArrayList())
        resRecyclerViewDay.adapter=weDayAdapter
        resRecyclerViewDay.layoutManager=layoutManagerDay

       // favoriteAdapter = FavoriteAdapter(context!!,this,ArrayList())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view =inflater.inflate(R.layout.fragment_result_favorite, container, false)

        val bundle = arguments
        val lati = bundle?.getDouble("recLat")
        val longi = bundle?.getDouble("recLong")
        receivedLat = lati
        receivedLong = longi
        Log.d("TAG", "onCreate: LATITUE in maps " + receivedLat)
        Log.d("TAG", "onCreate: LONGITUDE in maps" + receivedLong)


        //send to api
        if (receivedLat!=null&&receivedLong!=null) {
            resPreference = Preference.getInstance(context!!.applicationContext)!!
            resPreference.sendPreference("LATITUDE", receivedLat!!.toLong())
            resPreference.sendPreference("LONGITUDE", receivedLong!!.toLong())
        }





        //declare views
        resRecyclerViewHour= view.findViewById(R.id.resRecyclerViewHour)
        resRecyclerViewDay=view.findViewById(R.id.resRecyclerViewDay)
        resLocationCurrentTV=view.findViewById(R.id.resLocationCurrentTV)
        resDateCurrentTV=view.findViewById(R.id.resDateCurrentTV)
        resDescriptionCurrentTV=view.findViewById(R.id.resDescriptionCurrentTV)
        resTempCurrentTV=view.findViewById(R.id.resTempCurrentTV)
        resIconCurrentImg=view.findViewById(R.id.resIconCurrentImg)
        resActualPressureTV=view.findViewById(R.id.resActualPressureTV)
        resActualHumidityTV=view.findViewById(R.id.resActualHumidityTV)
        resActualWindTV=view.findViewById(R.id.resActualWindTV)
        resActualCloudTV=view.findViewById(R.id.resActualCloudTV)
        resActualUltraVioletTV=view.findViewById(R.id.resActualUltraVioletTV)
        resActualVisibilityTV=view.findViewById(R.id.resActualVisibilityTV)

        onHourViewModelFactory = OnHourViewModelFactory(
            WeatherRepository.getRepoInstance(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(this.requireContext()),
                this.context!!
            ),this.context!!
        )
        hourViewModel =
            ViewModelProvider(this, onHourViewModelFactory).get(OnHourViewModel::class.java)


        onHourViewModelFactory = OnHourViewModelFactory(
            WeatherRepository.getRepoInstance(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(this.requireContext()),
                this.context!!
            ),this.context!!
        )

//        favoriteViewModelFactory = FavoriteViewModelFactory(
//            WeatherRepository(
//                WeatherClient.getClientInstance()!!,
//                ConcreteLocalSource(requireContext()),
//                requireContext()
//            ),requireContext()
//        )
//
//        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)


//        receivedLocation= favoriteAdapter.location
//        Log.d("TAG", "onCreateView: receivedlocation"+receivedLocation.latitude)
//        Log.d("TAG", "onCreateView: receivedlocation"+receivedLocation.longitude)







        setUpRecyclerView()
       // doFav()
        setUpRetrofit()

        return view
    }


    private fun convertToData(dt:Double):String{
        val date = Date((dt*1000).toLong())
        val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        return format.format(date)
    }
//
    override fun onClick(favWeather: FavWeather) {
//        favoriteViewModel.getFavoriteWeather()
        //Toast.makeText(requireContext(),"Itemmmmmmmmmm", Toast.LENGTH_SHORT).show()
    }

    override fun onItem(favWeather: FavWeather) {
        receivedLocation= LatLng(favWeather.lattitude,favWeather.longitude)
    }


//    private fun doFav() {
//        favoriteViewModel.favoriteWeatherList.observe(this) {
//            if (it != null) {
//                favoriteAdapter.setFavorite(it)
//            }
//        }
//    }
}