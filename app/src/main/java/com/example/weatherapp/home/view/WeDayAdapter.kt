package com.example.weatherapp.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeDayAdapter(
val context: Context,
private val dayWeatherArrayList: ArrayList<Weather.Daily>)
: RecyclerView.Adapter<WeDayAdapter.DayViewHolder>() {


    fun setDataDay(dayList: List<Weather.Daily>) {
        this.dayWeatherArrayList.apply {
            clear()
            addAll(dayList)
            notifyDataSetChanged()
        }
    }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): WeDayAdapter.DayViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false)
            return DayViewHolder(view)
        }


        override fun onBindViewHolder(holder: WeDayAdapter.DayViewHolder, position: Int) {
            //trial remain the celcuis icon
            var day = convertToData(dayWeatherArrayList[position].dt)
            var url = "https://openweathermap.org/img/wn/${dayWeatherArrayList[position].weather[0].icon}@2x.png"
            Log.d("TAG", "onBindViewHolder: imageicon"+url)
            holder.dayTV.text=day
            holder.weatherDescTV.text=dayWeatherArrayList[position].weather[0].description
            holder.minTV.text=dayWeatherArrayList[position].temp.min.toString()
            holder.maxTV.text=dayWeatherArrayList[position].temp.max.toString()
            holder.weatherDescTV.text=dayWeatherArrayList[position].weather[0].description
            Glide.with(context).load(url).into(holder.dayImageView)

        }

        private fun convertToData(dt:Double):String{
        val date = Date((dt*1000).toLong())
        val format = SimpleDateFormat("EEE",Locale.ENGLISH)
        return format.format(date)
         }

        override fun getItemCount(): Int {
            return dayWeatherArrayList.size
        }

        inner class DayViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
            //after item update
            val dayTV : TextView = itemView.findViewById(R.id.dayTV)
            val weatherDescTV : TextView = itemView.findViewById(R.id.weatherDescTV)
            val minTV : TextView = itemView.findViewById(R.id.minTV)
            val maxTV : TextView = itemView.findViewById(R.id.maxTV)
            val cTV : TextView = itemView.findViewById(R.id.cTV)
            val dayImageView : ImageView = itemView.findViewById(R.id.dayImageView)

        }






    }

















