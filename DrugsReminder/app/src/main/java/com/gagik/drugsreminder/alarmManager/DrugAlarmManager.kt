package com.gagik.drugsreminder.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log

class DrugAlarmManager(private val context: Context) {
    companion object {
        private val TAG = "+++DrugAlarmManager"
        const val DRUG_NAME = "drug_name"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun createAlarm(drugName: String, time: Calendar) {
        val intent = Intent(context, DrugAlarmReceiver::class.java).apply {
            putExtra(DRUG_NAME, drugName)
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        try {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Log.i(TAG, "createAlarm: e = $e")
        }

    }

    fun cancelAlarm(drugName: String) {
        // Create an intent to broadcast when the alarm triggers
        val intent = Intent(context, DrugAlarmReceiver::class.java).apply {
            putExtra(DRUG_NAME, drugName)
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }
}