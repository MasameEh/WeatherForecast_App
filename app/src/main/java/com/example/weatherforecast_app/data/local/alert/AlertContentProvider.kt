package com.example.weatherforecast_app.data.local.alert

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.room.Room
import com.example.weatherforecast_app.data.local.AlertsDao
import com.example.weatherforecast_app.data.local.AppDatabase
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.runBlocking

class AlertContentProvider : ContentProvider() {


    private lateinit var dao: AlertsDao

    override fun onCreate(): Boolean {
        val context = context ?: return false

        val db = Room.databaseBuilder(context, AppDatabase::class.java, "roomdb").build()
        dao = db.getAlertsDao()

        return true
    }


    override  fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "timestamp"))
        cursor.addRow(arrayOf("1", "2"))
        val alerts = dao.getAllAlerts()

        runBlocking {
            alerts.collect { alertInfos ->
                alertInfos.forEach {
                    cursor.addRow((arrayOf(it.id, it.timestamp)))
                }
            }
        }
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}