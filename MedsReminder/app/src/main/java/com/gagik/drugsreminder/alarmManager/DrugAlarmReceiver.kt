package com.gagik.drugsreminder.alarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DrugAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get the drug name from the intent
        val drugName = intent.getStringExtra(DrugAlarmManager.DRUG_NAME) ?: return

        Log.d("DrugAlarmReceiver", "Alarm received for $drugName")

        // Show a notification to the user
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(drugName)
    }
}