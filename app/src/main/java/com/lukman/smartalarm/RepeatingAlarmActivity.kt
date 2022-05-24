package com.lukman.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lukman.smartalarm.data.Alarm
import com.lukman.smartalarm.data.local.AlarmDB
import com.lukman.smartalarm.data.local.AlarmDao
import com.lukman.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.lukman.smartalarm.helper.TAG_TIME_PICKER
import com.lukman.smartalarm.helper.timeFormatter
import kotlinx.android.synthetic.main.activity_one_time_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepeatingAlarmActivity : AppCompatActivity(), TimePickerFrament.TimeDialogListener{

    private var _binding :ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding

    private var alarmDao: AlarmDao? = null

    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(this)
        alarmDao = db.alarmDao()

        initView()
    }

    private fun initView() {

        binding.apply {
            btnSetTimeRepeating.setOnClickListener {
                val timePickerFrament = TimePickerFrament()
                timePickerFrament.show(supportFragmentManager, TAG_TIME_PICKER)
            }

            btnAddSetRepeatingAlarm.setOnClickListener {
                val time = tv_once_time.text.toString()
                val message = et_note_one_time.text.toString()

                if (time != "Time") {
                    CoroutineScope(Dispatchers.IO).launch {
                        alarmDao?.addAlarm(Alarm(
                            0,
                            "Repeating Alarm",
                            time,
                            message,
                            AlarmService.TYPE_REPEATING
                        ))
                    }
                } else {
                    Toast.makeText(
                        this@RepeatingAlarmActivity,
                        "Please set time of alarm.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    alarmDao?.addAlarm(Alarm(
                        0,
                        "Repeating Alarm",
                        time,
                        message,
                        AlarmService.TYPE_REPEATING
                    ))
                }
            }
            btnCancelSetRepeatingAlarm.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvRepeatingTime.text = timeFormatter(hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}