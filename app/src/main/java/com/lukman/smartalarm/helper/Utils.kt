package com.lukman.smartalarm.helper

import java.text.SimpleDateFormat
import java.util.*

const val TAG_TIME_PICKER = "TimePicker"

fun timeFormatter(hourOfDay: Int, minute: Int) : String{ //sebagai pengati
    val calendar = Calendar.getInstance()
    calendar.set(0,0,0,hourOfDay,minute)
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
}