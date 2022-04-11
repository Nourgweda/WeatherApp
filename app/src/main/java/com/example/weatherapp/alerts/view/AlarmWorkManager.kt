package com.example.weatherapp.alerts.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import androidx.work.WorkManager
import com.example.weatherapp.R
import com.example.weatherapp.db.ConcreteLocalSource
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.model.Alarm
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.WeatherClient
import com.example.weatherapp.preferences.Preference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class AlarmWorkManager(context: Context , workerParameters: WorkerParameters):Worker(context,workerParameters) {
    lateinit var alarm : List<Alarm>
    var number = 0
    lateinit var preference: Preference
    lateinit var weatherRepository:WeatherRepository


    private fun parseJSON(string: String?) {
        val gson = Gson()
        val type = object : TypeToken<List<Alarm?>?>() {}.type
        alarm = gson.fromJson(string, type)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val data = inputData
        val now = LocalDateTime.now()
        Log.d("TAG", "doWork: TIME NOW IS"+ now.hour)


        Log.d("TAG", "doWork: before ifffff inside work manager"+(data.getInt("timeNow", 0) == now.minute + now.hour).toString())
        if (data.getInt("timeNow", 0) == now.minute + now.hour) return Result.success()
        parseJSON(data.getString("alertList"))
        number= data.getInt("alertPosition", 0)
        Log.d("TAG", "ALARMMMMM " +alarm)

        fetchData()
        doAlarm(alarm)
        return Result.success()

    }






    fun ShowNotifications(context: Context, title: String?, message: String?, requestCode: Int){
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val CHANNEL_ID = "10"
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(
            requestCode,
            notificationBuilder.build()
        )
        Log.d("showNotification", "showNotification:"+ requestCode)

    }

    @JvmName("setAlarm1")
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun doAlarm(alarmList: List<Alarm>?) {
        WorkManager.getInstance().cancelAllWorkByTag("alarm")
        Log.d("TAG", "INSIDE WORKMANAGER doAlarm:## first do alarm ##")
        if (alarmList != null && !alarmList.isEmpty()) {
            Log.d("TAG", "INSIDE WORKMANAGER doAlarm:## first do alarm inside if condition##")
            val gson = Gson()
            val alarmListString = gson.toJson(alarmList)
            val timeN = LocalDateTime.now()
            var timeAt = LocalDate.now().atTime(
                Integer.valueOf(alarmList[0].hour),
                Integer.valueOf(alarmList[0].minute)
            )
            var duration = Duration.between(timeN, timeAt)
            var number = 0
            for (i in alarmList.indices) {
                timeAt = LocalDate.now().atTime(
                    Integer.valueOf(alarmList[i].hour),
                    Integer.valueOf(alarmList[i].minute)

                )
                Log.d("TAG", "INSIDE WORKMANAGER doAlarm:## first do alarm inside if condition##"+timeAt.hour)
                Log.d("TAG", "INSIDE WORKMANAGER doAlarm:## first do alarm inside if condition##"+timeAt.minute)

                //check today between start date and end date
                if (Calendar.getInstance().time.time >= alarmList[i].startMillies
                    && Calendar.getInstance().time.time <= alarmList[i].endMillies) {
                    if (timeAt.isAfter(timeN) && duration.abs().toMillis() > Duration.between(
                            timeN,
                            timeAt
                        ).toMillis()
                    ) {
                        duration = Duration.between(timeN, timeAt)
                        number = i
                    }
                }
            }
            val data = Data.Builder()
                .putString("alertList", alarmListString)
                .putInt("alertPosition", number)
                .putInt("timeNow", timeN.minute + timeN.hour)
                .build()

            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                AlarmWorkManager::class.java
            )
                .setInitialDelay(duration)
                .setInputData(data)
                .addTag("alarm")
                .build()
            WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        }
    }


    private fun fetchData() {
        ShowNotifications(applicationContext, alarm!![number].id + "", "Weather Alerts", 100)
        //trial

        preference = Preference.getInstance(applicationContext)!!

        weatherRepository =  WeatherRepository.getRepoInstance(
            WeatherClient.getClientInstance()!!,
            ConcreteLocalSource(applicationContext),
            applicationContext
        )

        CoroutineScope(Dispatchers.IO).launch {
            val response = weatherRepository.getWeather(preference.getPreference("LATITUDE")!!.toDouble(), preference.getPreference("LONGITUDE")!!.toDouble())

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TAG", "INSIDE WORKMANAGER getWeatherDetails:" +response.raw().request().url())
                    val desc = response.body()?.alerts?.get(0)?.description
                    Log.d("TAG", "INSIDE WORKMANAGER fetchData:"+desc)

                    if (desc != null){
                        ShowNotifications(applicationContext,"Weather Alert", desc,100)
                    }
                    else{
                        Log.d("TAG", "fetchData: BEFORE ELSE INSIDE WORKMANAGER ")
                        ShowNotifications(applicationContext,"Weather Alert", "NO ALERT AVAILABLE",100)
                    }

                } else {
                    Log.d("TAG", "INSIDE WORKMANAGER fetchData:"+response.message())
                }
            }
        }




    }








}