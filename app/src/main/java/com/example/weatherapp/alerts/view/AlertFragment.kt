package com.example.weatherapp.alerts.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.alerts.viewmodel.AlarmViewModel
import com.example.weatherapp.alerts.viewmodel.AlarmViewModelFactory
import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.ConcreteLocalSource
import com.example.weatherapp.favorites.view.FavoriteAdapter
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.home.view.HomeActivity
import com.example.weatherapp.model.Alarm
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.network.WeatherClient
import java.util.*


class AlertFragment : Fragment(),OnAlarmClickListener {

    lateinit var plusAlarmTV:TextView
    //alarm dialog
    var selectedHour:Int=0
    var selectedMinute:Int=0
     var format: String? = null
    var pickerStart: DatePickerDialog? = null
    var pickerEnd: DatePickerDialog? = null

    //add to room
    lateinit var alarmRecyclerView : RecyclerView
    lateinit var alarmViewModel: AlarmViewModel
    lateinit var alarmAdapter: AlarmAdapter
    lateinit var layoutManager: LinearLayoutManager
    var appDataBase: AppDatabase? = null
    lateinit var alarmViewModelFactory: AlarmViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_alert, container, false)
        alarmRecyclerView=view.findViewById(R.id.alarmRecyclerView)

        plusAlarmTV= view.findViewById(R.id.plusAlarmTV)
        plusAlarmTV.setOnClickListener {
            showDialog()

        }
        appDataBase = AppDatabase.getInstance(context!!)
        alarmViewModelFactory = AlarmViewModelFactory(
            WeatherRepository.getRepoInstance(
                WeatherClient.getClientInstance()!!,
                ConcreteLocalSource(context!!),
                context!!
            ),context!!
        )

        alarmViewModel = ViewModelProvider(this,alarmViewModelFactory).get(AlarmViewModel::class.java)

        setUpRecyclerView()
        doFav()






        return view
    }

    private fun doFav() {
        alarmViewModel.alarmList.observe(this) {
            if (it != null) {
                alarmAdapter.setAlarm(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        layoutManager = LinearLayoutManager(context!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        alarmAdapter = AlarmAdapter(context!!,this,ArrayList())
        alarmRecyclerView.adapter = alarmAdapter
        alarmRecyclerView.layoutManager = layoutManager    }


    private fun showDialog() {


        //sucess dialog
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.alarm_dialog)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.alarm_dialog)
        //declaring id
        val closeBtn = dialog.findViewById<TextView>(R.id.closeBtn)
        val nameAlarmED = dialog.findViewById<EditText>(R.id.nameAlarmED)
        val startDateTV = dialog.findViewById<TextView>(R.id.startDateTV)
        val endDateTV = dialog.findViewById<TextView>(R.id.endDateTV)
        val setAlarmTV = dialog.findViewById<TextView>(R.id.setAlarmTV)
        val showStartDate = dialog.findViewById<TextView>(R.id.showStartDate)
        val showEndDate = dialog.findViewById<TextView>(R.id.showEndDate)
        val showAlarm = dialog.findViewById<TextView>(R.id.showAlarm)
        val doneAlarmBtn = dialog.findViewById<Button>(R.id.doneAlarmBtn)

        closeBtn.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
        //trial
        setAlarmTV.setOnClickListener {
            val onTimeSetListener =
                OnTimeSetListener { _, hour, minute ->
                    selectedHour = hour
                    selectedMinute = minute
                    if (selectedHour == 0) {
                        selectedHour += 12
                        format = "AM"
                    } else if (selectedHour == 12) {
                        format = "PM"
                    } else if (hour > 12) {
                        selectedHour -= 12
                        format = "PM"
                    } else {
                        format = "AM"
                    }
                    showAlarm.text=selectedHour.toString()+":"+selectedMinute.toString()+":"+format.toString()

                    showAlarm.visibility = View.VISIBLE
                }
            val timePickerDialog = TimePickerDialog(
                view!!.context,
                TimePickerDialog.THEME_HOLO_LIGHT,
                onTimeSetListener,
                selectedHour,
                selectedMinute,
                false
            )
            timePickerDialog.setTitle("Set Alarm")
            timePickerDialog.show()
        }
        //trial
        startDateTV.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            pickerStart = DatePickerDialog(
                context!!,
                { view, year, monthOfYear, dayOfMonth ->
                    val strStart = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    showStartDate.setText(strStart)
                }, year, month, day
            )
            pickerStart!!.show()
            showStartDate.visibility = View.VISIBLE

        }
        //
        endDateTV.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            pickerEnd = DatePickerDialog(
                context!!,
                { view, year, monthOfYear, dayOfMonth ->
                    val strEnd = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    showEndDate.setText(strEnd)
                }, year, month, day
            )
            pickerEnd!!.show()
            showEndDate.visibility = View.VISIBLE

        }

        doneAlarmBtn.setOnClickListener {
            val name= nameAlarmED.text.trim().toString()
            val strStart=showStartDate.text.trim().toString()
            val strEnd=showEndDate.text.trim().toString()
            val alarm=showAlarm.text.trim().toString()
            if(!name.isEmpty()&&!strStart.isEmpty()&&!strEnd.isEmpty()&&!alarm.isEmpty()){
                if(selectedHour!=null &&selectedMinute!=null&&format!=null) {
                    dialog.dismiss()
                    Log.d("TAG", "showDialog: item is done" + name)
                    Log.d("TAG", "showDialog: item is done" + strStart)
                    Log.d("TAG", "showDialog: item is done" + strEnd)
                    Log.d("TAG", "showDialog: item is done" + alarm)

                    val ID: String = name + selectedHour + strStart + selectedMinute.toString()
                    val alarm = Alarm(
                        name,
                        strStart,
                        strEnd,
                        selectedHour.toString(),
                        selectedMinute.toString(),
                        format!!,
                        ID
                    )
                    Toast.makeText(requireContext(), "location is saved", Toast.LENGTH_SHORT).show()
                    alarmViewModel.insertAlarm(alarm)
                    Log.d("TAG", "onCreateView: LOCATION SAVED IN ROOM")
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            }
            else{
                Toast.makeText(requireContext(),"Please Fill All Fields", Toast.LENGTH_SHORT).show()
            }

        }





















    }

    override fun onClick(alarm: Alarm) {
        alarmViewModel.deleteAlarm(alarm)
        Toast.makeText(requireContext(),"Item Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onItem(alarm: Alarm) {
//        alarmViewModel.deleteAlarm(alarm)
//        Toast.makeText(requireContext(),"Item Deleted", Toast.LENGTH_SHORT).show()
    }


}