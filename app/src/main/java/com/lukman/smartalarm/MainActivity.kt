package com.lukman.smartalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lukman.smartalarm.databinding.ActivityMainBinding
import com.lukman.smartalarm.adapter.AlarmAdapter
import com.lukman.smartalarm.data.local.AlarmDB
import com.lukman.smartalarm.data.local.AlarmDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var alarmDao : AlarmDao? = null
    private var alarmAdapter : AlarmAdapter? = null

    private var alarmService : AlarmService? = null

    override fun onResume() {
        super.onResume()
        alarmDao?.getAlarm()?.observe(this){ lambdaDariObserve ->
            alarmAdapter?.setData(lambdaDariObserve)
        }

        /*CoroutineScope(Dispatchers.IO).launch{
            val alarm = alarmDao?.getAlarm()
            withContext(Dispatchers.Main){
                alarm?.let { alarmAdapter?.setData(it) }
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(application)
        alarmDao = db.alarmDao()

        alarmAdapter = AlarmAdapter()

        initView()
        setupRecylerView()
    }

    private fun setupRecylerView() {
        binding.rvReminderAlarm.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
            swipeToDelete(this)
        }
    }

    private fun initView(){
        binding.apply {
            cvSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(applicationContext, OneTimeAlarmActivity::class.java))
            }
            cvSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
            }


//            getTimeToday()
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //TODO 3 -> notifyitemremoved hapus!
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deletedAlarm?.let { alarmDao?.deleteAlarm(it)}
                }
                val alarmType = deletedAlarm?.type
                alarmType?.let { alarmService?.cancelAlarm(baseContext, it) }
                alarmAdapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView((recyclerView))
    }
}

    /*private fun getTimeToday(){ //dd/M/yyyy hh:mm:ss
        val calendar = Calendar.getInstance() //getInstance untuk menginisialisasi
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()) // HH:MM = hour.Minute
        val time = formattedTime.format(calendar.time)

//        https://stackoverflow.com/questions/47006254/how-to-get-current-local-date-and-time-in-kotlin

        binding.tvTimeToday.text = time
    }*/
