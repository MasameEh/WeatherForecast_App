package com.example.weatherforecast_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast_app.data.model.LocationInfo


@Database(entities = [LocationInfo::class], version = 1)
abstract class LocationsDatabase : RoomDatabase(){
    abstract fun getLocationsDao(): LocationsDao

    companion object{
        @Volatile
        private var instance: LocationsDatabase?  = null

//        fun getInstance(context: Context): LocationsDatabase{
//            return instance ?: synchronized(this){
//                val inst = Room.databaseBuilder(context, LocationsDatabase::class.java, "roomdb").build()
//                instance = inst // Stores the created instance for future calls
//                inst
//            }
//        }
        fun getInstance(context: Context): LocationsDatabase{
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(context, LocationsDatabase::class.java, "roomdb").build()
                    .also {
                        instance = it  // Stores the created instance for future calls
                    }
            }
        }
    }
}