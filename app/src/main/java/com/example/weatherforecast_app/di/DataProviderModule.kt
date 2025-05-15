package com.example.weatherforecast_app.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecast_app.data.local.AlertsDao
import com.example.weatherforecast_app.data.local.AppDatabase
import com.example.weatherforecast_app.data.local.LocationsDao
import com.example.weatherforecast_app.data.remote.location.LocationService
import com.example.weatherforecast_app.data.remote.weather.WeatherService
import com.example.weatherforecast_app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataProviderModule {

    @Provides
    @Singleton
    fun provideRetrofitWeather(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitLocation(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideLocationService(retrofitLocation: Retrofit): LocationService {
        return retrofitLocation.create(LocationService::class.java)
    }

    @Provides
    fun provideWeatherService(retrofitWeather: Retrofit): WeatherService {
        return retrofitWeather.create(WeatherService::class.java)
    }


    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "roomdb").build()
    }

    @Provides
    fun provideLocationDao(db: AppDatabase): LocationsDao{
        return db.getLocationsDao()
    }

    @Provides
    fun provideAlertsDao(db: AppDatabase): AlertsDao{
        return db.getAlertsDao()
    }
}