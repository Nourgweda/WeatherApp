package com.example.weatherapp.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.FavWeather

class FavoriteAdapter(
    val context: Context,
    val onFavClickListener: OnFavClickListener,
    private val favoriteArrayList: ArrayList<FavWeather>)
: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(){

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
        holder.cityNameTV.text=favoriteArrayList[position].location
        holder.deleteFavorite.setOnClickListener {
            onFavClickListener.onClick(favoriteArrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return favoriteArrayList.size
    }


    inner class FavoriteViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        //after item update
        val cityNameTV : TextView = itemView.findViewById(R.id.cityNameTV)
        val deleteFavorite:ImageView=itemView.findViewById(R.id.deleteFavorite)
    }




}