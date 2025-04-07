package com.gagik.drugsreminder.common.repository

import android.content.Context
import android.util.Log
import com.gagik.drugsreminder.common.database.DrugsDatabase
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import kotlinx.coroutines.flow.first


class Repository private constructor(context: Context) {
    companion object {
        private const val TAG = "+++Repository"
        private var INSTANCE: Repository? = null

        fun getRepository(context: Context): Repository {
            return INSTANCE ?: Repository(context).also {
                INSTANCE = it
            }
        }
    }

    private val drugsDatabase: DrugsDatabase = DrugsDatabase.getDatabase(context)
    private val drugsDao = drugsDatabase.drugsDao

    suspend fun tmpPrintAllDrugs() {
        drugsDao.getDrugs().collect {
            Log.i(TAG, "tmpPrintAllDrugs: it = $it")
        }
    }

    suspend fun addNewDrug(drugsEntity: DrugsEntity) = drugsDao.insertDrug(drugsEntity)

    fun getAllDrugs() = drugsDao.getDrugs()

    fun getDrugById(id: Long) = drugsDao.getDrugById(id)

    fun deleteDrugById(id: Long): Unit = drugsDao.deleteDrugById(id)

}