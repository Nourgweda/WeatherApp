package com.example.weatherapp.favorites.view

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.db.ConcreteLocalSource
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.home.view.HomeFragment
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.RemoteSource
import com.example.weatherapp.network.WeatherClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*
import kotlin.math.log

class FavMapsFragment : Fragment() {
    lateinit var gooMap:GoogleMap
    var currentLocationMarker: Marker?=null

    var receivedLatitude:Double?=null
    var receivedLongitude:Double?=null
    var latlong: LatLng? = null
    var ID: String? = null

    lateinit var favSearchED:EditText
    lateinit var favDoneBtn:Button
    lateinit var favSearchBtn:ImageView
    lateinit var favWeather:FavWeather
    lateinit var locationName :String
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        gooMap=googleMap
//        if(receivedLatitude!=null&&receivedLongitude!=null) {
//            latlong = LatLng(receivedLatitude!!, receivedLongitude!!)
//            putMarker(latlong!!)
//            gooMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
//                override fun onMapClick(L: LatLng) {
//                    if (currentLocationMarker != null) {
//                        currentLocationMarker?.remove()
//                    }
//                    Log.d("TAG", "onMarkerDragEnd: inside method on map ")
//                    //val latlong = LatLng(M?.position!!.latitude, M?.position.longitude)
//                    latlong= LatLng(L?.latitude,L?.longitude)
//                    Log.d("TAG", "onMap clicked: AFTER CHANGING POSITION"+latlong!!.latitude)
//                    Log.d("TAG", "onMap clicked:AFTER CHANGING POSITION "+latlong!!.longitude)
//                    putMarker(latlong!!)
//                }
//
//
//
//            })
//
//    }

        gooMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(L: LatLng) {
                if (currentLocationMarker != null) {
                    currentLocationMarker?.remove()
                }
                Log.d("TAG", "onMarkerDragEnd: inside method on map ")
                //val latlong = LatLng(M?.position!!.latitude, M?.position.longitude)
                latlong= LatLng(L?.latitude,L?.longitude)
                Log.d("TAG", "onMap clicked: AFTER CHANGING POSITION"+latlong!!.latitude)
                Log.d("TAG", "onMap clicked:AFTER CHANGING POSITION "+latlong!!.longitude)
                putMarker(latlong!!)
                Log.d("TAG", "searchLocation: FINAL ADDRESS "+latlong!!.latitude)
                Log.d("TAG", "searchLocation: FINAL ADDRESS"+latlong!!.longitude)
            }



        })



}
    private fun putMarker(latlong: LatLng) {
        Log.d("TAG", "putMarker: first")
        val markerOptions=MarkerOptions().position(latlong).title("location is")
            .snippet(getAddress(latlong.latitude,latlong.longitude)).draggable(true)

        gooMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        gooMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
        currentLocationMarker=gooMap.addMarker(markerOptions)
        currentLocationMarker?.showInfoWindow()
        Log.d("TAG", "putMarker: last")
    }

    private fun getAddress(latitude: Double, longitude: Double): String? {

        val geocoder=Geocoder(context, Locale.getDefault())
        val address= geocoder.getFromLocation(latitude,longitude,1)
        return address[0].getAddressLine(0).toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_fav_maps, container, false)
        favSearchED=view.findViewById(R.id.favSearchED)
        favDoneBtn=view.findViewById(R.id.favDoneBtn)
        favSearchBtn=view.findViewById(R.id.favSearchBtn)


        favoriteViewModelFactory = FavoriteViewModelFactory(
            WeatherRepository(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(requireContext()),
                requireContext()
            ),requireContext()
        )
        favoriteViewModel= ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        val bundle = arguments
        val lati = bundle?.getDouble("latitude")
        val longi= bundle?.getDouble("longitude")
        receivedLatitude=lati
        receivedLongitude=longi


        favSearchBtn.setOnClickListener {
            searchLocation(view)
        }

        favDoneBtn.setOnClickListener {
            //favWeather= FavWeather(latlong!!.latitude,latlong!!.longitude,locationName,0)
//            favWeather.lattitude=0.0
//            favWeather.longitude=0.0
//            favWeather.location=""
            if (!favSearchED.text.toString().isNullOrEmpty()) {
                if (latlong != null) {

                    ID=latlong.toString()
                   favWeather = FavWeather(latlong!!.latitude, latlong!!.longitude, locationName,ID!!)
//                       favWeather=FavWeather()
//                    favWeather.lattitude=latlong!!.latitude
//                    favWeather.longitude=latlong!!.longitude
//                    favWeather.location=locationName


                    Log.d("TAG", "onCreateView: FINAL LOCATION" + latlong!!.latitude)
                    Log.d("TAG", "onCreateView: FINAL LOCATION" + latlong!!.longitude)
                    Log.d("TAG", "onCreateView: FINAL LOCATION" + locationName)
                    Toast.makeText(requireContext(), "location is saved", Toast.LENGTH_SHORT).show()
                    favoriteViewModel.insertWeather(favWeather)
                    Log.d("TAG", "onCreateView: LOCATION SAVED IN ROOM")
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
//                    val favoriteFragment=FavoriteFragment()
//                    val transaction = fragmentManager!!.beginTransaction()
//                    transaction.replace(R.id.fragmentContainerView, favoriteFragment)
//                    transaction.addToBackStack(null)
//                    transaction.commit()
                    activity!!.finish()
                }

            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun searchLocation(view: View) {
        lateinit var location: String
        location = favSearchED.text.toString()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(requireContext(),"provide location",Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(requireContext())
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            locationName=address.featureName+","+address.countryName

//            gooMap!!.addMarker(MarkerOptions().position(latLng).title(location))
//            gooMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            putMarker(latLng!!)
            //Toast.makeText(requireContext(), address.latitude.toString() + " " + address.longitude, Toast.LENGTH_LONG).show()
            getAddress(address.latitude,address.longitude)
            Log.d("TAG", "searchLocation: "+address.latitude)
            Log.d("TAG", "searchLocation: "+address.longitude)
//            receivedLatitude=address.latitude
//            receivedLongitude=address.longitude
            latlong=latLng
            Log.d("TAG", "searchLocation: FINAL ADDRESS "+latlong!!.latitude)
            Log.d("TAG", "searchLocation: FINAL ADDRESS"+latlong!!.longitude)
        }
    }


}
