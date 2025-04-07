package com.gagik.drugsreminder.common.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDrug(drugsEntity: DrugsEntity)

    @Query("SELECT * FROM drugs_table")
    fun getDrugs(): Flow<List<DrugsEntity>>

    @Query("SELECT * FROM drugs_table WHERE id = :id")
    fun getDrugById(id: Long): Flow<DrugsEntity>

    @Query("DELETE FROM drugs_table WHERE id = :id")
    fun deleteDrugById(id: Long)
}