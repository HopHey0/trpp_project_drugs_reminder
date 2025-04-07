package com.gagik.drugsreminder.common.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drugs_table")
data class DrugsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String?,
    @ColumnInfo(name = "active_factor")
    var activeFactor: String?,
    @ColumnInfo(name = "amount_substance")
    var amountSubstance: String?,
    var contraindications: String?,
    @ColumnInfo(name = "side_effects")
    var sideEffects: String?
)
