package com.example.weatherapp.settings.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.weatherapp.R
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.home.view.HomeFragment
import kotlin.math.log


class SettingsFragment(
    val location:Int,
    val temperature:Int,
    val language:Int,
    val wind:Int,
    val notification:Int

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
    lateinit var onRadio: RadioButton
    lateinit var offRadio: RadioButton

    var recLocation:Int=0
    var recTemperature:Int=0
    var recLanguage:Int=0
    var recWind:Int=0
    var recNotification:Int=0


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
        onRadio=view.findViewById(R.id.onRadio)
        offRadio=view.findViewById(R.id.offRadio)

        recLocation=this.location
        Log.d("TAG", "onCreateView: receviede location"+recLocation)
        recTemperature=this.temperature
        Log.d("TAG", "onCreateView: receviede temperature"+recTemperature)
        recLanguage=this.language
        Log.d("TAG", "onCreateView: receviede language"+recLanguage)
        recWind=this.wind
        Log.d("TAG", "onCreateView: receviede wind"+recWind)
        recNotification=this.notification
        Log.d("TAG", "onCreateView: receviede notification"+recNotification)

        if (recLocation==1){
            gpsRadio.setChecked(true)
            mapRadio.setChecked(false)
        }
        if(recLocation==2){
            gpsRadio.setChecked(false)
            mapRadio.setChecked(true)
        }
        gpsRadio.setOnClickListener {
            !mapRadio.isChecked
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("home",1)
            startActivity(intent)
            activity!!.finish()
        }
        mapRadio.setOnClickListener {
            !gpsRadio.isChecked
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("home",2)
            startActivity(intent)
            activity!!.finish()
        }

















        return view
    }


}