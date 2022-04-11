package com.example.weatherapp.settings.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.weatherapp.R
import com.example.weatherapp.favorites.view.FavMapsFragment
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.home.view.HomeFragment
import com.example.weatherapp.preferences.Preference
import kotlin.math.log


class SettingsFragment(
//    val location:Int,
//    val temperature:Int,
//    val language:Int,
//    val wind:Int,
//    val notification:Int

) : Fragment() {


    lateinit var gpsRadio: RadioButton
    lateinit var mapRadio: RadioButton
    lateinit var celTempRadio: RadioButton
    lateinit var kelvTempRadio: RadioButton
    lateinit var fahTempRadio: RadioButton
    lateinit var engRadio: RadioButton
    lateinit var arabicRadio: RadioButton
    lateinit var secondRadio: RadioButton
    lateinit var hourRadio: RadioButton

    var recLocation:Int=0
    var recLanguage:Int=0
    var recTemperature:Int=0
    var recWind:Int=0
    var recNotification:Int=0


    lateinit var sharedPreferences: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_settings, container, false)
        //declaring radiobuttons
        gpsRadio=view.findViewById(R.id.gpsRadio)
        mapRadio=view.findViewById(R.id.mapRadio)
        celTempRadio=view.findViewById(R.id.celTempRadio)
        kelvTempRadio=view.findViewById(R.id.kelvTempRadio)
        fahTempRadio=view.findViewById(R.id.fahTempRadio)
        engRadio=view.findViewById(R.id.engRadio)
        arabicRadio=view.findViewById(R.id.arabicRadio)
        secondRadio=view.findViewById(R.id.secondRadio)
        hourRadio=view.findViewById(R.id.hourRadio)
//        onRadio=view.findViewById(R.id.onRadio)
//        offRadio=view.findViewById(R.id.offRadio)


        sharedPreferences = Preference.getInstance(context!!.applicationContext)!!

        recLocation=sharedPreferences.getSettings("HOMELoc")!!
        Log.d("TAG", "onCreateView:from settings "+ recLocation)
        recLanguage=sharedPreferences.getSettings("SendLanguage")!!
        recTemperature=sharedPreferences.getSettings("SendTemp")!!




        if (recLocation==1){
            gpsRadio.setChecked(true)
            mapRadio.setChecked(false)
        }
        if(recLocation==2){
            gpsRadio.setChecked(false)
            mapRadio.setChecked(true)
        }


        //Location
        gpsRadio.setOnClickListener {
            gpsRadio.setChecked(true)
            mapRadio.setChecked(false)
            sharedPreferences.sendSettings("LOCATION",1)
            goBigHome()
        }
        mapRadio.setOnClickListener {
            gpsRadio.setChecked(false)
            mapRadio.setChecked(true)
            sharedPreferences.sendSettings("LOCATION",2)
            goBigHome()
        }


        //Temperature
        if (recTemperature==3){
            kelvTempRadio.setChecked(false)
            fahTempRadio.setChecked(false)
            celTempRadio.setChecked(true)
        }
        if(recTemperature==4){
            kelvTempRadio.setChecked(true)
            fahTempRadio.setChecked(false)
            celTempRadio.setChecked(false)
        }
        if (recTemperature==5){
            kelvTempRadio.setChecked(false)
            fahTempRadio.setChecked(true)
            celTempRadio.setChecked(false)
        }

        //speed only radio button
        if (recTemperature==3||recTemperature==4){
            hourRadio.setChecked(false)
            secondRadio.setChecked(true)
        }
        if (recTemperature==5){
            hourRadio.setChecked(true)
            secondRadio.setChecked(false)
        }


        celTempRadio.setOnClickListener {
            celTempRadio.setChecked(true)
            kelvTempRadio.setChecked(false)
            fahTempRadio.setChecked(false)
            hourRadio.setChecked(false)
            secondRadio.setChecked(true)
            sharedPreferences.sendSettings("TEMPERATURE",3)
            goBigHome()
        }

        kelvTempRadio.setOnClickListener {
            celTempRadio.setChecked(false)
            fahTempRadio.setChecked(false)
            kelvTempRadio.setChecked(true)
            hourRadio.setChecked(false)
            secondRadio.setChecked(true)
            sharedPreferences.sendSettings("TEMPERATURE",4)
            goBigHome()
        }

        fahTempRadio.setOnClickListener {
            celTempRadio.setChecked(false)
            kelvTempRadio.setChecked(false)
            fahTempRadio.setChecked(true)
            hourRadio.setChecked(true)
            secondRadio.setChecked(false)
            sharedPreferences.sendSettings("TEMPERATURE",5)
            goBigHome()
        }


        secondRadio.setOnClickListener {
            celTempRadio.setChecked(false)
            fahTempRadio.setChecked(false)
            kelvTempRadio.setChecked(true)
            hourRadio.setChecked(false)
            secondRadio.setChecked(true)
            sharedPreferences.sendSettings("TEMPERATURE",4)
            goBigHome()
        }

        hourRadio.setOnClickListener {
            celTempRadio.setChecked(false)
            kelvTempRadio.setChecked(false)
            fahTempRadio.setChecked(true)
            hourRadio.setChecked(true)
            secondRadio.setChecked(false)
            sharedPreferences.sendSettings("TEMPERATURE",5)
            goBigHome()
        }




        //language
        if (recLanguage==6){
            engRadio.setChecked(true)
            arabicRadio.setChecked(false)
        }

        if (recLanguage==7){
            engRadio.setChecked(false)
            arabicRadio.setChecked(true)
        }

        engRadio.setOnClickListener {
            engRadio.setChecked(true)
            arabicRadio.setChecked(false)
            sharedPreferences.sendSettings("LANGUAGE",6)
            goBigHome()
        }

        arabicRadio.setOnClickListener {
            engRadio.setChecked(false)
            arabicRadio.setChecked(true)
            sharedPreferences.sendSettings("LANGUAGE",7)
            goBigHome()
        }















        return view
    }


    fun goHome(){
        val homeFragment=HomeFragment()
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun goBigHome(){
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

}