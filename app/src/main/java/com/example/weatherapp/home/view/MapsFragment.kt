package com.example.weatherapp.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import com.example.weatherapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment(){
     lateinit var gooMap:GoogleMap
    var currentLocationMarker: Marker?=null
//    internal lateinit var lastLocation: Location
//    internal var currentLocationMarker: Marker?=null
//    internal var gooAPIClient :GoogleApiClient?=null
//    internal lateinit var locationRequest: LocationRequest
     var receivedLatitude:Double?=null
    var receivedLongitude:Double?=null
    var latlong: LatLng? = null


    @SuppressLint("PotentialBehaviorOverride")
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

        gooMap=googleMap
        val loc = receivedLatitude?.let { receivedLongitude?.let { it1 -> LatLng(it, it1) } }
//        loc?.let { MarkerOptions().position(it).title("Marker in Sydney") }?.let {
//            googleMap.addMarker(
//                it
//            )
//        }
//        loc?.let { CameraUpdateFactory.newLatLng(it) }?.let { googleMap.moveCamera(it) }
////        gooMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
//        loc?.let {CameraUpdateFactory.newLatLngZoom(loc,20f)}

        //loc?.let {  animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18), 5000, null)}
        //trial
        if(receivedLatitude!=null&&receivedLongitude!=null) {
            latlong = LatLng(receivedLatitude!!, receivedLongitude!!)
            putMarker(latlong!!)
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
                }



            })




//            gooMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
//                override fun onMarkerDrag(M: Marker) {
//                    if (currentLocationMarker != null) {
//                        currentLocationMarker?.remove()
//                    }
//
//                    Log.d("TAG", "onMarkerDragEnd: inside method on marker drag ")
//                    val latlong = LatLng(M?.position!!.latitude, M?.position.longitude)
//                    Log.d("TAG", "onMarkerDragEnd: AFTER CHANGING POSITION"+latlong.latitude)
//                    Log.d("TAG", "onMarkerDragEnd:AFTER CHANGING POSITION "+latlong.longitude)
//                    putMarker(latlong)
//                }
//
//                override fun onMarkerDragEnd(M: Marker) {
//                    if (currentLocationMarker != null) {
//                        currentLocationMarker?.remove()
//                    }
//
//                    Log.d("TAG", "onMarkerDragEnd: inside method on marker drag ")
//                    val latlong = LatLng(M?.position!!.latitude, M?.position.longitude)
//                    Log.d("TAG", "onMarkerDragEnd: AFTER CHANGING POSITION"+latlong.latitude)
//                    Log.d("TAG", "onMarkerDragEnd:AFTER CHANGING POSITION "+latlong.longitude)
//                    putMarker(latlong)
//                }
//
//                override fun onMarkerDragStart(M: Marker) {
//                    if (currentLocationMarker != null) {
//                        currentLocationMarker?.remove()
//                    }
//
//                    Log.d("TAG", "onMarkerDragEnd: inside method on marker drag ")
//                    val latlong = LatLng(M?.position!!.latitude, M?.position.longitude)
//                    Log.d("TAG", "onMarkerDragEnd: AFTER CHANGING POSITION"+latlong.latitude)
//                    Log.d("TAG", "onMarkerDragEnd:AFTER CHANGING POSITION "+latlong.longitude)
//                    putMarker(latlong)
//                }
//
//            })


        }

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
        val view= inflater.inflate(R.layout.fragment_maps, container, false)
        val bundle = arguments
        val lati = bundle?.getDouble("latitude")
        val longi= bundle?.getDouble("longitude")
        receivedLatitude=lati
        receivedLongitude=longi
        Log.d("TAG", "onCreate: LATITUE in maps "+receivedLatitude)
        Log.d("TAG", "onCreate: LONGITUDE in maps"+receivedLongitude)
        //button ok
        val mapOkBtn: Button = view.findViewById(R.id.mapOkBtn)
        mapOkBtn.setOnClickListener {
            Log.d("TAG", "onCreateView: ok is clicked  ")
            Log.d("TAG","location here "+latlong!!)
            val lati:Double = latlong!!.latitude
            val longi:Double=latlong!!.longitude
            val bundle = Bundle()
            bundle.putSerializable("latitude", lati)
            bundle.putSerializable("longitude", longi)
            passData(bundle)
        }





        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    fun passData(bundle: Bundle){
        val homeFragment = HomeFragment()
        homeFragment.setArguments(bundle)
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        //activity?.finish()

    }







}
