package com.example.weatherapp.preferences

import android.content.Context
import android.content.SharedPreferences

class Preference (var context: Context) {
    var preference: SharedPreferences

    init {
        preference=context.getSharedPreferences("preferenceName",Context.MODE_PRIVATE)
    }

    companion object{
        private var instance:Preference?=null
        fun getInstance(context: Context):Preference?{
            return instance?: Preference(context)
        }
    }

    fun sendPreference (key:String?, value:Long?){
        val preferenceSended = preference.edit()
        preferenceSended.putLong(key,value!!)
        preferenceSended.apply()
    }

    fun getPreference(key:String?):Long?{
        return if(preference!=null){
            preference.getLong(key,0)
        }else
            0
    }

//    fun putDouble(edit: SharedPreferences.Editor, key: String?, value: Double): SharedPreferences.Editor? {
//        return edit.putLong(key, java.lang.Double.doubleToRawLongBits(value))
//    }
//
//    fun getDouble(prefs: SharedPreferences, key: String?, defaultValue: Double): Double {
//        return java.lang.Double.longBitsToDouble(
//            prefs.getLong(
//                key,
//                java.lang.Double.doubleToLongBits(defaultValue)
//            )
//        )
//    }


}