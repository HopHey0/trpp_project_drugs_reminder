package com.gagik.drugsreminder.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.gagik.drugsreminder.R
import com.gagik.drugsreminder.common.repository.Repository
import com.gagik.drugsreminder.ui.fragments.ReminderFragment
import com.gagik.drugsreminder.utils.requestNotificationPermission
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission(this)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, ReminderFragment.newInstance())
            .commit()
    }
}

