package com.example.weatherapp.alerts.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.favorites.view.FavoriteAdapter
import com.example.weatherapp.model.Alarm

class AlarmAdapter(
    val context: Context,
val onAlarmClickListener: OnAlarmClickListener,
private val alarmArrayList: ArrayList<Alarm>)
    : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    fun setAlarm(alarmList: List<Alarm>) {
        this.alarmArrayList.apply {
            clear()
            addAll(alarmList)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmAdapter.AlarmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmAdapter.AlarmViewHolder, position: Int) {
        holder.alarmName.text = alarmArrayList[position].name
        holder.deleteAlarm.setOnClickListener {
            onAlarmClickListener.onClick(alarmArrayList[position])
        }
//        holder.alarmItem.setOnClickListener {
//            onAlarmClickListener.onItem(alarmArrayList[position])
//        }
//
//        holder.alarmItem.setOnClickListener {
//            onAlarmClickListener.onClick(alarmArrayList[position])
//        }


    }

    override fun getItemCount(): Int {
        return alarmArrayList.size
    }


    inner class AlarmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        //after item update
        val alarmName: TextView = itemView.findViewById(R.id.alarmName)
        val deleteAlarm: ImageView = itemView.findViewById(R.id.deleteAlarm)
        val alarmItem: CardView = itemView.findViewById(R.id.alarmItem)


    }

}