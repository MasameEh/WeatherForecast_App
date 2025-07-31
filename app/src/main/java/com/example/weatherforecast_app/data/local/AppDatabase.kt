package com.example.weatherforecast_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast_app.data.model.AlertInfo
import com.example.weatherforecast_app.data.model.LocationInfo


@Database(entities = [LocationInfo::class, AlertInfo::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getLocationsDao(): LocationsDao
    abstract fun getAlertsDao(): AlertsDao

    companion object{
        @Volatile
        private var instance: AppDatabase?  = null

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "roomdb").build()
                    .also {
                        instance = it  // Stores the created instance for future calls
                    }
            }
        }
    }
}