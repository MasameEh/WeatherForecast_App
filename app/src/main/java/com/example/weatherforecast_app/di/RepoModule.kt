package com.example.weatherforecast_app.di

import com.example.weatherforecast_app.data.local.Alert.AlertLocalDataSourceImp
import com.example.weatherforecast_app.data.local.location.LocationLocalDataSourceImp
import com.example.weatherforecast_app.data.remote.location.LocationRemoteDataSourceImp
import com.example.weatherforecast_app.data.remote.weather.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.alert_repo.AlertRepositoryImp
import com.example.weatherforecast_app.data.repo.location_repo.LocationRepositoryImp
import com.example.weatherforecast_app.data.repo.weather_repo.WeatherRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindILocationLocalDataSource(): LocationLocalDataSourceImp

    @Binds
    abstract fun bindIAlertLocalDataSource(): AlertLocalDataSourceImp


    @Binds
    abstract fun bindILocationRemoteDataSource(): LocationRemoteDataSourceImp

    @Binds
    abstract fun bindIWeatherRemoteDataSource(): WeatherRemoteDataSourceImp

    @Binds
    abstract fun bindIAlertRepository(): AlertRepositoryImp

    @Binds
    abstract fun bindILocationRepository(): LocationRepositoryImp

    @Binds
    abstract fun bindIWeatherRepository(): WeatherRepositoryImp

}