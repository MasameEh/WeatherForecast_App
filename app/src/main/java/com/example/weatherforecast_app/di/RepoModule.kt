package com.example.weatherforecast_app.di

import com.example.weatherforecast_app.data.local.alert.AlertLocalDataSourceImp
import com.example.weatherforecast_app.data.local.alert.IAlertLocalDataSource
import com.example.weatherforecast_app.data.local.location.ILocationLocalDataSource
import com.example.weatherforecast_app.data.local.location.LocationLocalDataSourceImp
import com.example.weatherforecast_app.data.remote.location.ILocationRemoteDataSource
import com.example.weatherforecast_app.data.remote.location.LocationRemoteDataSourceImp
import com.example.weatherforecast_app.data.remote.weather.IWeatherRemoteDataSource
import com.example.weatherforecast_app.data.remote.weather.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.alert_repo.AlertRepositoryImp
import com.example.weatherforecast_app.data.repo.alert_repo.IAlertRepository
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.example.weatherforecast_app.data.repo.location_repo.LocationRepositoryImp
import com.example.weatherforecast_app.data.repo.weather_repo.IWeatherRepository
import com.example.weatherforecast_app.data.repo.weather_repo.WeatherRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindILocationLocalDataSource(locationLocalDataSource: LocationLocalDataSourceImp): ILocationLocalDataSource

    @Binds
    abstract fun bindIAlertLocalDataSource(alertLocalDataSource: AlertLocalDataSourceImp): IAlertLocalDataSource


    @Binds
    abstract fun bindILocationRemoteDataSource(locationRemoteDataSource: LocationRemoteDataSourceImp): ILocationRemoteDataSource

    @Binds
    abstract fun bindIWeatherRemoteDataSource(weatherRemoteDataSource: WeatherRemoteDataSourceImp): IWeatherRemoteDataSource

    @Binds
    abstract fun bindIAlertRepository(alertRepository: AlertRepositoryImp): IAlertRepository

    @Binds
    abstract fun bindILocationRepository(locationRepository: LocationRepositoryImp): ILocationRepository

    @Binds
    abstract fun bindIWeatherRepository(weatherRepository: WeatherRepositoryImp): IWeatherRepository

}