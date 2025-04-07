package com.gagik.drugsreminder.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestNotificationPermission(activity: Activity) {
    // Check if the notification permission has already been granted
    if (ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Permission has already been granted, do nothing
        return
    }

    // Request the notification permission
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
        REQUEST_CODE_NOTIFICATION_PERMISSION
    )
}

//The request code for the notification permission request
private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 582