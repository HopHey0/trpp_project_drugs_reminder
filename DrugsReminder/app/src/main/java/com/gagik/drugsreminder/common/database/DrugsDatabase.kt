package com.gagik.drugsreminder.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gagik.drugsreminder.common.database.dao.DrugsDao
import com.gagik.drugsreminder.common.database.entities.DrugsEntity

@Database(
    entities = [DrugsEntity::class],
    version = 1
)
abstract class DrugsDatabase : RoomDatabase() {
    abstract val drugsDao: DrugsDao

    companion object {

        private var DB_INSTANCE: DrugsDatabase? = null

        fun getDatabase(context: Context): DrugsDatabase {
            synchronized(this) {
                return DB_INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DrugsDatabase::class.java,
                    "drugs_db"
                )
                    .addCallback(PrepopulateRoomCallback(context))
                    .build()
                    .also {
                        DB_INSTANCE = it
                    }
            }
        }
    }
}