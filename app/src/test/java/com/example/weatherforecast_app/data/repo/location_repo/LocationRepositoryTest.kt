package com.example.weatherforecast_app.data.repo.location_repo

import com.example.weatherforecast_app.data.local.location.ILocationLocalDataSource
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.remote.location.ILocationRemoteDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test



class LocationRepositoryTest {

    //1. local datasource, remote datasource
    private lateinit var stubLocalDataSource: ILocationLocalDataSource
    private lateinit var stubRemoteDataSource: ILocationRemoteDataSource
    private lateinit var dummyFusedLocationClient: FusedLocationProviderClient
    private lateinit var repo: ILocationRepository

    @Before
    fun setup(){
        stubLocalDataSource = mockk(relaxed = true)
        stubRemoteDataSource = mockk(relaxed = true)
        dummyFusedLocationClient = mockk(relaxed = true)
        repo = LocationRepositoryImp.getInstance(dummyFusedLocationClient, stubLocalDataSource, stubRemoteDataSource)

        coEvery {
            stubLocalDataSource.insertLocation(
                any()
            )
        } returns 5
        coEvery {
            stubLocalDataSource.deleteLocation(
                any()
            )
        } returns 5

        every {
            repo.getAllFavLocations()
        } returns flowOf(
            listOf(
                LocationInfo("Egypt", "Cairo", 30.0444, 31.2357),
                LocationInfo("Egypt", "Zagazig", 30.112, 31.44),
                LocationInfo("USA", "New York", 40.7128, -74.0060)
            )
        )
    }


    @Test
    fun insertLocation_locationInfo_returnsFive() = runTest {

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        val res = repo.insertLocation(location)

        // Then (assert)
        assertThat(res, `is`(5) )
    }

    @Test
    fun deleteLocation_locationInfo_returnsFive() = runTest{

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        repo.insertLocation(location)
        val res = repo.deleteLocation(location)

        // Then (assert)
        assertThat(res, `is`(5) )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllLocations_noUpdate_allLocations() = runTest{

        // Given (arrange)
        val res = mutableListOf<LocationInfo>()

        // When (act)
        val job = launch {
            repo.getAllFavLocations().collect{
                res.addAll(it)
            }
        }

        advanceUntilIdle()

        // Then (assert)
        assertThat(res.size, `is`(3) )
        assertThat(res[0].country, `is`("Egypt") )
        assertThat(res[2].country, `is`("USA") )

        // Stop collecting
        job.cancel()
    }
}