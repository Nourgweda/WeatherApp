package com.example.weatherapp.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeHourAdapter (
    val context: Context,
    //val onMovieClickListener: OnHourClickListener,
    private val hourWeatherArrayList: ArrayList<Weather.Hourly>
) : RecyclerView.Adapter<WeHourAdapter.HourViewHolder>(){

    fun setDataHour(hourList: List<Weather.Hourly>) {
        this.hourWeatherArrayList.apply {
            clear()
            addAll(hourList)
            notifyDataSetChanged()
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeHourAdapter.HourViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hour_item, parent, false)
        return HourViewHolder(view)
    }


    override fun onBindViewHolder(holder: WeHourAdapter.HourViewHolder, position: Int) {

//        holder.hourTV.text=hourWeatherArrayList[position].dt.toString()
        //holder.tempTV.text=hourWeatherArrayList[position].temp.toString()
        //Glide.with(context).load(hourWeatherArrayList[position].weather[position].icon).into(holder.tempImageView)

        //"h:mm a"
        var hour = convertToData(hourWeatherArrayList[position].dt)
        Log.d("TAG", "convertToData:HOUR IS = "+ hour)
        holder.hourTV.text=hour
        holder.tempTV.text=hourWeatherArrayList[position].temp.toString()
        var url = "https://openweathermap.org/img/wn/${hourWeatherArrayList[position].weather[0].icon}@2x.png"
        Glide.with(context).load(url).into(holder.tempImageView)
    }

    override fun getItemCount(): Int {
        return hourWeatherArrayList.size
    }

    private fun convertToData(dt:Double):String{
        val date = Date((dt*1000).toLong())
        val format = SimpleDateFormat("h a", Locale.ENGLISH)
        return format.format(date)
    }

    inner class HourViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hourTV : TextView = itemView.findViewById(R.id.hourTV)
        val tempImageView : ImageView = itemView.findViewById(R.id.tempImageView)
        val tempTV : TextView = itemView.findViewById(R.id.tempTV)
    }


}