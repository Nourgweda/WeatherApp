package com.example.weatherapp.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.alerts.AlertFragment
import com.example.weatherapp.favorites.FavoriteFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenu

class HomeActivity : AppCompatActivity(),GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


//    ,OnMapReadyCallback
//    , LocationListener,
//    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

    var starterRadioCheck:Int =0
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object{
        private const val  Permission_Request = 100
    }

    var sendedLocationLati : Double?=null
    var sendedLocationLongi : Double?=null


    //google maps objects
    private var gooMap:GoogleMap?=null
    internal lateinit var lastLocation: Location
    internal var currentLocationMarker:Marker?=null
    internal var gooAPIClient :GoogleApiClient?=null
    internal lateinit var locationRequest: LocationRequest


    //bottom navigation
    private lateinit var currentFragment: Fragment



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //trial location
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        //start dialog to set up settings
        showDialog()
//        if(starterRadioCheck==2){
//            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//            mapFragment.getMapAsync(this)
//            val fragmentManager: FragmentManager = supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            val myFragment = MapsFragment()
//            val bundle = Bundle()
//            sendedLocationLati?.let { bundle.putDouble("latitude", it)}
//            sendedLocationLongi?.let{bundle.putDouble("longitude",it)}
//            myFragment.arguments = bundle
//            fragmentTransaction.add(R.id.fragmentContainerView, myFragment).commit()
//
//            Log.d("TAG", "onCreate: LATITUE"+sendedLocationLati)
//            Log.d("TAG", "onCreate: LONGITUDE"+sendedLocationLongi)


//        }
        //trial
//        if(starterRadioCheck==1){
////            val fragmentManager: FragmentManager = supportFragmentManager
////            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
////            val homeFragment = HomeFragment()
////            val bundle = Bundle()
////            sendedLocationLati?.let { bundle.putDouble("latitude", it)}
////            sendedLocationLongi?.let{bundle.putDouble("longitude",it)}
////            homeFragment.arguments = bundle
////            fragmentTransaction.add(R.id.fragmentContainerView, homeFragment).commit()
////            Log.d("TAG", "onCreate: LATITUE"+sendedLocationLati)
////            Log.d("TAG", "onCreate: LONGITUDE"+sendedLocationLongi)
//                            val bundle = Bundle()
//                            bundle.putSerializable("latitude",sendedLocationLati)
//                            bundle.putSerializable("longitude",sendedLocationLongi)
//            findNavController(view).navigate(
//                R.id.action_addMedicationPrimary_to_addMedicationFinal,
//                bundle
//            )
//
//
//
//
//
//        }


        //bottom navigation
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()
        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener)





    }


    val navigationListener=BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.homeIcon-> {
                currentFragment=HomeFragment()
            }
            R.id.favoriteIcon->{
                currentFragment=FavoriteFragment()
            }

            R.id.alertIcon->{
                currentFragment= AlertFragment()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,currentFragment).commit()
        true

    }



    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.start_dialog)
        val doneBtn = dialog.findViewById<Button>(R.id.doneBtn)
        val gpsRadioBtn=dialog.findViewById<RadioButton>(R.id.gpsRadioBtn)
        val mapRadioBtn=dialog.findViewById<RadioButton>(R.id.mapRadioBtn)
        gpsRadioBtn.setOnClickListener {
            mapRadioBtn.setChecked(false)
            Log.d("TAG", "showDialog: gps checked")
            starterRadioCheck=1;
            getLocation()
        }
        mapRadioBtn.setOnClickListener {
            gpsRadioBtn.setChecked(false)
            Log.d("TAG", "showDialog: map checked")
            starterRadioCheck=2
//            if(starterRadioCheck==2){
////                            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
////                            mapFragment.getMapAsync(this)
////                val intent:Intent = Intent(this,MapsActivity::class.java)
////                startActivity(intent)
//            }

            getLocation()


//            val fragmentManager: FragmentManager = supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            val myFragment = MapsFragment()
//            val bundle = Bundle()
//            sendedLocationLati?.let { bundle.putDouble("latitude", it)}
//            sendedLocationLongi?.let{bundle.putDouble("longitude",it)}
//            myFragment.arguments = bundle
//            fragmentTransaction.add(R.id.fragmentContainerView, myFragment).commit()
//            Log.d("TAG", "onCreate: LATITUE"+sendedLocationLati)
//            Log.d("TAG", "onCreate: LONGITUDE"+sendedLocationLongi)

           // dialog.dismiss()
        }
        doneBtn.setOnClickListener {
            Log.d("TAG", "showDialog:starter number is "+starterRadioCheck)
            dialog.dismiss()
        }
        dialog.show()
    }


    //search for current location using GPS
    private fun getLocation(){
        //if permission is true
        if(checkPermission()){
            //if location is set on mobile is true
            if(locationEnabled()){
                //get the longitute and latitude
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){
                        task->
                    //check if the location get nullable to get result of the task
                    val location:Location?=task.result
                    if (location==null){
                        Log.d("TAG", "getLocation: didnt get location")
                    }
                    else{
                        Log.d("TAG", "getLocation: latitude {${location.latitude} longitute {${location.longitude}}}")
                        //sendedLocation= LatLng(location.latitude,location.longitude)
                        sendedLocationLati=location.latitude
                        sendedLocationLongi=location.longitude
                        if(starterRadioCheck==1){
                            val fragmentManager: FragmentManager = supportFragmentManager
                            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                            val homeFragment = HomeFragment()
                            val bundle = Bundle()
                            sendedLocationLati?.let { bundle.putDouble("latitude", it)}
                            sendedLocationLongi?.let{bundle.putDouble("longitude",it)}
                            homeFragment.arguments = bundle
                            fragmentTransaction.add(R.id.fragmentContainerView, homeFragment).commit()
                            Log.d("TAG", "onCreate: LATITUE"+sendedLocationLati)
                            Log.d("TAG", "onCreate: LONGITUDE"+sendedLocationLongi)
//                            val bundle = Bundle()
//                            bundle.putSerializable("latitude",sendedLocationLati)
//                            bundle.putSerializable("longitude",sendedLocationLongi)





                        }
                        if(starterRadioCheck==2){
                            val fragmentManager: FragmentManager = supportFragmentManager
                            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                            val mapsFragment = MapsFragment()
                            val bundle = Bundle()
                            sendedLocationLati?.let { bundle.putDouble("latitude", it)}
                            sendedLocationLongi?.let{bundle.putDouble("longitude",it)}
                            mapsFragment.arguments = bundle
                            fragmentTransaction.add(R.id.fragmentContainerView, mapsFragment).commit()
                            Log.d("TAG", "onCreate: LATITUE"+sendedLocationLati)
                            Log.d("TAG", "onCreate: LONGITUDE"+sendedLocationLongi)
                        }


                        //----trial---//show fragment if location is here and if he choose map button
//                        if(starterRadioCheck==2){
////                            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
////                            mapFragment.getMapAsync(this)
//                        val intent:Intent = Intent(this,MapsActivity::class.java)
//                        startActivity(intent)
//                        }
                    }
                }
            }
            else{
                //open mobile setting
                Log.d("TAG", "getLocation: location must be turned on")
                val intent= Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            //request permission
            requestPermission()
        }
    }






    private fun locationEnabled(): Boolean {
        val locationManager:LocationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    //get permission
    private fun requestPermission() {
        //we save it in array of because if we have multiple permissions
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION), Permission_Request)
    }

    //check permission
    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==
            PackageManager.PERMISSION_GRANTED&&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED){
            return  true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Permission_Request){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("TAG", "onRequestPermissionsResult: GRANTED ")
                getLocation()
            }
            else{
                Log.d("TAG", "onRequestPermissionsResult: DENIED")
            }
        }
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        gooMap=googleMap
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission
//                    .ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//                buildGoogleApiClient()
//                gooMap!!.isMyLocationEnabled=true
//            }
//        }else{
//            buildGoogleApiClient()
//            gooMap!!.isMyLocationEnabled=true
//        }
//    }


    protected fun buildGoogleApiClient(){
        gooAPIClient= GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        gooAPIClient!!.connect()
    }

//    override fun onLocationChanged(location: Location) {
//        lastLocation=location
//        if(currentLocationMarker!=null){
//            currentLocationMarker!!.remove()
//        }
//        val longLati = LatLng(location.latitude,location.longitude)
//        val markerOptions = MarkerOptions()
//        markerOptions.position(longLati)
//        markerOptions.title("Position")
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//        currentLocationMarker=gooMap!!.addMarker(markerOptions)
//
//        gooMap!!.moveCamera(CameraUpdateFactory.newLatLng(longLati))
//        gooMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
//
//        if(gooAPIClient!=null){
//            LocationServices.getFusedLocationProviderClient(this)
//        }
//    }
//
    override fun onConnected(p0: Bundle?) {
        locationRequest= LocationRequest()
        locationRequest.interval=1000
        locationRequest.fastestInterval=1000
        locationRequest.priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED){
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


//    fun searchLocation(view:View){
//        val searchLocationET:EditText=findViewById(R.id.searchLocationET)
//        var searchLocation : String
//        searchLocation =searchLocationET.text.toString().trim()
//        var locationList : List<Address>?=null
//        if(searchLocation.isNullOrEmpty()){
//            Toast.makeText(this,"Enter a Location Please",Toast.LENGTH_SHORT).show()
//        }else{
//            val geoCoder = Geocoder(this)
//            try{
//                locationList=geoCoder.getFromLocationName(searchLocation,1)
//            }catch (exception:IOException){
//                exception.printStackTrace()
//            }
//            val address=locationList!![0]
//            val longilati= LatLng(address.latitude,address.longitude)
//            gooMap!!.addMarker(MarkerOptions().position(longilati).title(searchLocation))
//            gooMap!!.animateCamera(CameraUpdateFactory.newLatLng(longilati))
//        }
//    }
}

