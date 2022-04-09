package com.example.weatherapp.favorites.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.ConcreteLocalSource
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.WeatherClient
import com.example.weatherapp.preferences.Preference
import com.google.android.gms.maps.model.LatLng


class FavoriteFragment : Fragment() ,OnFavClickListener{



    lateinit var favoriteRecyclerView : RecyclerView
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var layoutManager: LinearLayoutManager
    var appDataBase: AppDatabase? = null
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var plusFavoriteTV: TextView
    lateinit var favMapsFragment:FavMapsFragment

//    lateinit var receivedLocation: LatLng
//    lateinit var resPreference: Preference
     var receivedLat: Double? = null
     var receivedLong: Double? = null

    var input:Int = 0

    var longi:Double = 0.0
    var lati:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favorite, container, false)

        //send to api
//        if(receivedLat!=null&&receivedLong!=null) {
//            resPreference = Preference.getInstance(context!!.applicationContext)!!
//            resPreference.sendPreference("LATITUDE", receivedLocation!!.latitude.toLong())
//            resPreference.sendPreference("LONGITUDE", receivedLocation!!.longitude.toLong())
//        }



        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)
        plusFavoriteTV=view.findViewById(R.id.plusFavoriteTV)
        appDataBase = AppDatabase.getInstance(context!!)
        favoriteViewModelFactory = FavoriteViewModelFactory(
            WeatherRepository.getRepoInstance(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(context!!),
                context!!
            ),context!!
        )

        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        setUpRecyclerView()
        doFav()

        plusFavoriteTV.setOnClickListener{
            val favMapsFragment = FavMapsFragment()
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, favMapsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }








        return view
    }

    private fun doFav() {
        favoriteViewModel.favoriteWeatherList.observe(this) {
            if (it != null) {
                favoriteAdapter.setFavorite(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        layoutManager = LinearLayoutManager(context!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        favoriteAdapter = FavoriteAdapter(context!!,this,ArrayList())
        favoriteRecyclerView.adapter = favoriteAdapter
        favoriteRecyclerView.layoutManager = layoutManager    }


    override fun onClick(favWeather: FavWeather) {
        favoriteViewModel.deleteWeather(favWeather)
        Toast.makeText(requireContext(),"Item Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onItem(favWeather: FavWeather) {
        //receivedLocation= LatLng(favWeather.lattitude,favWeather.longitude)
        receivedLat=favWeather.lattitude
        receivedLong=favWeather.longitude
        val bundle = Bundle()
        bundle.putDouble("recLat",receivedLat!!)
        bundle.putDouble("recLong",receivedLong!!)
        val resultFavoriteFragment = ResultFavoriteFragment()
        val transaction = fragmentManager!!.beginTransaction()
        resultFavoriteFragment.arguments = bundle
        transaction.replace(R.id.fragmentContainerView, resultFavoriteFragment)
        transaction.addToBackStack(null)
        transaction.commit()



    }


}