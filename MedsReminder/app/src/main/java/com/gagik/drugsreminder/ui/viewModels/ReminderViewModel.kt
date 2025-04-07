package com.gagik.drugsreminder.ui.viewModels

import android.icu.util.Calendar
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gagik.drugsreminder.R
import com.gagik.drugsreminder.alarmManager.DrugAlarmManager
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.utils.ResourceHelper

class ReminderViewModel(
    private val alarmManager: DrugAlarmManager,
    private val resourceHelper: ResourceHelper
) : ViewModel() {
    private var selectedHour: Int? = null
    private var selectedMinute: Int? = null
    private var selectedDrug: DrugsEntity? = null
    private var selectedWeekDay: Int = Calendar.MONDAY

    private val _selectedTimeText: MutableLiveData<String> =
        MutableLiveData(getString(R.string.preview_picked_time))
    val selectedTimeText: LiveData<String> = _selectedTimeText
    private val _selectedDrugText: MutableLiveData<String> =
        MutableLiveData(getString(R.string.drug_not_selected))
    val selectedDrugText: LiveData<String> = _selectedDrugText

    fun onTimeSelected(hour: Int, minute: Int) {
        selectedHour = hour
        selectedMinute = minute
        val selectedTime = dateFormatter(hour, minute)
        _selectedTimeText.value = selectedTime
    }

    fun onDrugSelected(drug: DrugsEntity) {
        selectedDrug = drug
        _selectedDrugText.value = drug.name ?: getString(R.string.drug_not_selected)
    }

    fun setAlarm(message: (String) -> Unit) {
        if (selectedHour == null || selectedMinute == null) {
            message.invoke(getString(R.string.please_select_time))
            return
        }

        if (selectedDrug == null) {
            message.invoke(getString(R.string.please_select_drug))
            return
        }

        val time = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, selectedWeekDay)
            set(Calendar.HOUR_OF_DAY, selectedHour!!)
            set(Calendar.MINUTE, selectedMinute!!)
        }

        alarmManager.createAlarm(selectedDrug!!.name ?: getString(R.string.drug), time)
        message.invoke(getString(R.string.reminder_set_successfully))
    }

    fun setWeekDay(day: Int) {
        selectedWeekDay = day
    }

    private fun dateFormatter(hour: Int, minute: Int): String {
        val formattedHour = if (hour < 10) "0$hour" else hour.toString()
        val formattedMinute = if (minute < 10) "0$minute" else minute.toString()
        return "$formattedHour:$formattedMinute"
    }

    private fun getString(@StringRes id: Int) = resourceHelper.getResources().getString(id)
}

class ReminderViewModelFactory(
    private val alarmManager: DrugAlarmManager,
    private val resourceHelper: ResourceHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            ReminderViewModel(this.alarmManager, this.resourceHelper) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}