package com.example.weatherapp.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.R
import com.example.weatherapp.alerts.view.AlertFragment
import com.example.weatherapp.favorites.view.FavoriteFragment
import com.example.weatherapp.preferences.Preference
import com.example.weatherapp.settings.view.SettingsFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import androidx.annotation.NonNull
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar


class HomeActivity : AppCompatActivity(),GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


//    ,OnMapReadyCallback
//    , LocationListener,
//    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

    var starterRadioCheck: Int = 0
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val Permission_Request = 100
    }

    var sendedLocationLati: Double? = null
    var sendedLocationLongi: Double? = null


    //google maps objects
    private var gooMap: GoogleMap? = null
    internal lateinit var lastLocation: Location
    internal var currentLocationMarker: Marker? = null
    internal var gooAPIClient: GoogleApiClient? = null
    internal lateinit var locationRequest: LocationRequest


    //bottom navigation
    private lateinit var currentFragment: Fragment

    //trial
    var flag: Boolean = true

    //send data to settings
    var setLoc: Int = 0
    var setLang: Int = 0

    var receivedLoc: Int = 0
    var receivedLanguage: Int = 0
    var receivedTemp: Int = 0
    var receivedWind: Int = 0
    var lang: String? = null
    var temp: String? = null
    lateinit var preference: Preference


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //preference for language
        preference = Preference.getInstance(this!!.applicationContext)!!
        receivedLanguage = preference.getSettings("LANGUAGE")!!
        if (receivedLanguage == 6) {
            lang = "en"
            LanguageOriginale(this, lang)
            preference.sendRepo("repo", lang)
            preference.sendSettings("SendLanguage", 6)
        }

        if (receivedLanguage == 7) {
            lang = "ar"
            LanguageOriginale(this, lang)
            preference.sendRepo("repo", lang)
            preference.sendSettings("SendLanguage", 7)
        }

        receivedTemp = preference.getSettings("TEMPERATURE")!!
        if (receivedTemp == 3) {
            temp = "metric"
            preference.sendRepo("temp", temp)
            preference.sendSettings("SendTemp", 3)
        }
        if (receivedTemp == 4) {
            temp = "standard"
            preference.sendRepo("temp", temp)
            preference.sendSettings("SendTemp", 4)
        }
        if (receivedTemp == 5) {
            temp = "imperial"
            preference.sendRepo("temp", temp)
            preference.sendSettings("SendTemp", 5)
        }



        if (receivedLanguage == 0) {
            preference.sendSettings("SendLanguage", 6)
        }
        if (receivedTemp == 0) {
            preference.sendSettings("SendTemp", 3)
        }


        setContentView(R.layout.activity_home)
        //received bundle
//        val bundle :Bundle ?=intent.extras
//        if (bundle!=null) {
//            val message = bundle.getInt("home")
//            Log.d("TAG", "onCreate:HERE HERE HERE HERE HERE " + message)
//            when(message){
//                1->{
//                    starterRadioCheck=1
//                }
//                2->{
//                    starterRadioCheck=2
//                }
//
//
//            }
//
//
//
//
//
//        }
        //receveid bundle


        //trial location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        //showDialog()


        getLocation()


        //bottom navigation
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment()).commit()
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener)


    }


    val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.homeIcon -> {
                currentFragment = HomeFragment()
            }
            R.id.favoriteIcon -> {
                currentFragment = FavoriteFragment()
            }

            R.id.alertIcon -> {
                currentFragment = AlertFragment()
            }
            R.id.settingsIcon -> {
                currentFragment = SettingsFragment()
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, currentFragment).commit()
        true

    }


    fun LanguageOriginale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())
    }


    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.start_dialog)
        val doneBtn = dialog.findViewById<Button>(R.id.doneBtn)
        val gpsRadioBtn = dialog.findViewById<RadioButton>(R.id.gpsRadioBtn)
        val mapRadioBtn = dialog.findViewById<RadioButton>(R.id.mapRadioBtn)
        gpsRadioBtn.setOnClickListener {
            mapRadioBtn.setChecked(false)
            Log.d("TAG", "showDialog: gps checked")
            starterRadioCheck = 1;
            getLocation()
        }
        mapRadioBtn.setOnClickListener {
            gpsRadioBtn.setChecked(false)
            Log.d("TAG", "showDialog: map checked")
            starterRadioCheck = 2

            getLocation()

        }
        doneBtn.setOnClickListener {
            Log.d("TAG", "showDialog:starter number is " + starterRadioCheck)
            dialog.dismiss()
        }
        dialog.show()
    }


    //search for current location using GPS
    fun getLocation() {
        //if permission is true
        Log.d("TAG", "getLocation: HERE AHOOOO")
        if (checkPermission()) {
            //if location is set on mobile is true
            //showDialog()
            if (locationEnabled()) {
                //get the longitute and latitude
                //showDialog()
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    //check if the location get nullable to get result of the task
                    val location: Location? = task.result
                    if (location == null) {
                        Log.d("TAG", "getLocation: didnt get location")
                    } else {
                        Log.d(
                            "TAG",
                            "getLocation: latitude {${location.latitude} longitute {${location.longitude}}}"
                        )
                        //sendedLocation= LatLng(location.latitude,location.longitude)
                        sendedLocationLati = location.latitude
                        sendedLocationLongi = location.longitude
                        //if (!flag) {
                        //trial preference
                        preference = Preference.getInstance(this)!!
                        receivedLoc = preference.getSettings("LOCATION")!!
                        Log.d("TAG", "getLocation:RECEIVED LOCATION " + receivedLoc)


                        if (starterRadioCheck == 1 || flag || receivedLoc == 1) {
                            val fragmentManager: FragmentManager = supportFragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            val homeFragment = HomeFragment()
                            val bundle = Bundle()
                            sendedLocationLati?.let { bundle.putDouble("latitude", it) }
                            sendedLocationLongi?.let { bundle.putDouble("longitude", it) }
                            homeFragment.arguments = bundle
                            fragmentTransaction.add(R.id.fragmentContainerView, homeFragment)
                                .commit()
                            Log.d("TAG", "onCreate: LATITUE" + sendedLocationLati)
                            Log.d("TAG", "onCreate: LONGITUDE" + sendedLocationLongi)
                            setLoc = 1

                        }
                        if (starterRadioCheck == 2 || receivedLoc == 2) {
                            val fragmentManager: FragmentManager = supportFragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            val mapsFragment = MapsFragment()
                            val bundle = Bundle()
                            sendedLocationLati?.let { bundle.putDouble("latitude", it) }
                            sendedLocationLongi?.let { bundle.putDouble("longitude", it) }
                            mapsFragment.arguments = bundle
                            fragmentTransaction.add(R.id.fragmentContainerView, mapsFragment)
                                .commit()
                            Log.d("TAG", "onCreate: LATITUE" + sendedLocationLati)
                            Log.d("TAG", "onCreate: LONGITUDE" + sendedLocationLongi)
                            setLoc = 2
                        }

                        preference.sendSettings("HOMELoc", setLoc)
                    }
                }
            } else {
                //open mobile setting
                Log.d("TAG", "getLocation: location must be turned on")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            //request permission
            requestPermission()
        }
    }


    private fun locationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        // flag

    }

    //get permission
    private fun requestPermission() {
        //we save it in array of because if we have multiple permissions
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), Permission_Request
        )
        // showDialog()
    }

    //check permission
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            //showDialog()
            return true
            // flag
            //showDialog()
        }
        return false
        //!flag
        //showDialog()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permission_Request) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "onRequestPermissionsResult: GRANTED ")
                flag
                //getLocation()
                showDialog()

            } else {
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


    protected fun buildGoogleApiClient() {
        gooAPIClient = GoogleApiClient.Builder(this)
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
        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }








    //trial notification permission



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