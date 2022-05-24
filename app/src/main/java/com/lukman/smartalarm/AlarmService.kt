package com.lukman.smartalarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        val type = intent?.getIntExtra(EXTRA_TYPE, 0)

        val title = when (intent?.getIntExtra(EXTRA_TYPE,0)) {
            TYPE_ONE_TIME -> "on time alarm"
            TYPE_REPEATING -> "repeating alarm"
            else -> "something wrong here"
        }

        val requestCode = when (type) {
            TYPE_ONE_TIME -> ID_ONE_TIME
            TYPE_REPEATING -> ID_REPEATING
            else -> -1
        }

        if (message != null) {
            showNotificationAlarm(
                context, "Alarm Oii", message, 101)
        }
    }

    fun cancelAlarm(context: Context, type: Int,) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmService::class.java)
        val requestCode = when (type) {
            TYPE_ONE_TIME -> ID_ONE_TIME
            TYPE_REPEATING -> ID_REPEATING
            else -> Log.d("CancelAlarm", "Unknown type of alarm")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        if (type == TYPE_ONE_TIME) {
            Toast.makeText(context,"One Time Alarm Canceled.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Repeating Alarm Canceled.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRepeatingalarm(context: Context,type: Int,time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split("-").toTypedArray()
        val calendar = Calendar.getInstance()
        // time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pendingIntent)
        Toast.makeText(context, "Success set OneTimeAlarm", Toast.LENGTH_SHORT).show()
    }

    fun setOnTimeAlarm(context:Context, type: Int, date : String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val dateArray = date.split("-").toTypedArray()

        val calendar = Calendar.getInstance()
        // date
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1])-1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]))
        // time
        calendar.set(Calendar.HOUR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(dateArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONE_TIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pendingIntent)
        Toast.makeText(context, "Success set OneTimeAlarm", Toast.LENGTH_SHORT).show()
    }


    private fun showNotificationAlarm(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelName = "SmartAlarm"
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val channelId = "smart_alarm"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, "alarm_1")
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(ringtone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("alarm_1", channelId, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId("alarm_1")
            notificationManager.createNotificationChannel(channel)
        }
        val notif = builder.build()
        notificationManager.notify(notificationId, notif)
    }

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val ID_ONE_TIME = 101
        const val ID_REPEATING = 102

        const val TYPE_ONE_TIME = 1
        const val TYPE_REPEATING = 0
    }
}