package com.gagik.drugsreminder.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gagik.drugsreminder.common.database.entities.DrugsEntity

class SharedViewModel: ViewModel() {
    val selectedDrug = MutableLiveData<DrugsEntity>()
}