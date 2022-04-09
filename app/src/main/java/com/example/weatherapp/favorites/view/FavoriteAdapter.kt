package com.example.weatherapp.favorites.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherapp.model.FavWeather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.LatLng

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.weatherapp.R
import com.example.weatherapp.home.view.HomeFragment
import kotlin.math.log


class FavoriteAdapter(
    val context: Context,
    val onFavClickListener: OnFavClickListener,
    private val favoriteArrayList: ArrayList<FavWeather>)
: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    lateinit var location: LatLng

    fun setFavorite(favoriteList: List<FavWeather>) {
        this.favoriteArrayList.apply {
            clear()
            addAll(favoriteList)
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return FavoriteViewHolder(view)
    }


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.cityNameTV.text = favoriteArrayList[position].location
        holder.deleteFavorite.setOnClickListener {
            onFavClickListener.onClick(favoriteArrayList[position])
        }
        holder.favoriteItem.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: 3AAAAAAAAAAAAA")
            onFavClickListener.onItem(favoriteArrayList[position])
        }








    }

    override fun getItemCount(): Int {
        return favoriteArrayList.size
    }


    inner class FavoriteViewHolder( private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        //after item update
        val cityNameTV: TextView = itemView.findViewById(R.id.cityNameTV)
        val deleteFavorite: ImageView = itemView.findViewById(R.id.deleteFavorite)
        val favoriteItem : CardView = itemView.findViewById(R.id.favoriteItem)

//        init {
//            itemView.setOnClickListener {
//                val position: Int = adapterPosition
//                Log.d("TAG", "onBindViewHolder: " + favoriteArrayList[position].location)
//
//            }
//        }


    }


}