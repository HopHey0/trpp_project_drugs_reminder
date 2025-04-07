package com.gagik.drugsreminder.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.common.repository.Repository
import kotlinx.coroutines.launch

class DrugsListViewModel(private val repository: Repository) : ViewModel() {
    fun getAllDrugs(allDrugs: (List<DrugsEntity>) -> Unit) {
        viewModelScope.launch {
            repository.getAllDrugs().collect {
                allDrugs.invoke(it)
            }
        }
    }
}


class RepositoryViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DrugsListViewModel::class.java) -> DrugsListViewModel(this.repository) as T
            modelClass.isAssignableFrom(AddNewDrugViewModel::class.java) -> AddNewDrugViewModel(this.repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}