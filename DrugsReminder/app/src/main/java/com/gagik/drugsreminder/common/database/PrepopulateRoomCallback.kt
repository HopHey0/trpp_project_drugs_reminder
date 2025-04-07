package com.gagik.drugsreminder.common.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gagik.drugsreminder.R
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {
    private val TAG = "+++PrepopulateRoomCallback"

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateDrugs(context)
        }
    }

    private suspend fun prePopulateDrugs(context: Context) {
        try {
            val userDao = DrugsDatabase.getDatabase(context).drugsDao

            val drugsList: JSONArray = context
                .resources
                .openRawResource(R.raw.drugs)
                .bufferedReader()
                .use {
                    JSONArray(it.readText())
                }

            drugsList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val drugsObj = list.getJSONObject(index)
                    val name = if (drugsObj.has("name")) drugsObj.getString("name") else null
                    val activeFactor =
                        if (drugsObj.has("active_factor")) drugsObj.getString("active_factor") else null
                    val amountSubstance =
                        if (drugsObj.has("amount_substance")) drugsObj.getString("amount_substance") else null
                    val contraindications =
                        if (drugsObj.has("contraindications")) drugsObj.getString("contraindications") else null
                    val sideEffects =
                        if (drugsObj.has("side_effects")) drugsObj.getString("side_effects") else null
                    userDao.insertDrug(
                        DrugsEntity(
                            id = drugsObj.getLong("id"),
                            name = name,
                            activeFactor = activeFactor,
                            amountSubstance = amountSubstance,
                            contraindications = contraindications,
                            sideEffects = sideEffects,
                        )
                    )

                }
                Log.e(TAG, "successfully pre-populated drug into database")
            }
        } catch (exception: Exception) {
            Log.e(
                TAG,
                exception.localizedMessage ?: "failed to pre-populate drug into database"
            )
        }
    }
}