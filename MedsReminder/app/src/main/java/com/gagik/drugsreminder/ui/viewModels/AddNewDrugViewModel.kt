package com.gagik.drugsreminder.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.common.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNewDrugViewModel(private val repository: Repository) : ViewModel() {
    fun saveDrug(drug: DrugsEntity, onDrugSaved: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewDrug(drug)
            withContext(Dispatchers.Main) {
                onDrugSaved.invoke()
            }
        }
    }

}