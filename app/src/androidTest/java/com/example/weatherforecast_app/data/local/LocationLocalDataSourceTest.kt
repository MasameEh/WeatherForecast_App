package com.example.weatherforecast_app.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherforecast_app.data.local.location.ILocationLocalDataSource
import com.example.weatherforecast_app.data.local.location.LocationLocalDataSourceImp
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.remote.location.ILocationRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class LocationLocalDataSourceTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: LocationsDao
    private lateinit var localDataSource: ILocationLocalDataSource

    @Before
    fun setUp(){
        // different database from the actual database, live as long as the test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries() // room refuses to do operation on main thread by default
            .build()

        dao = database.getLocationsDao()
        localDataSource = LocationLocalDataSourceImp(dao)
    }
    @After
    fun tearDown() = database.close()


    @Test
    fun insertLocation_locationInfo_returnsGreaterThanZero() = runTest {

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        val res = localDataSource.insertLocation(location)

        // Then (assert)
        assertThat(res, `is`(greaterThan(0)) )
    }

    @Test
    fun deleteLocation_locationInfo_returnsGreaterThanZero() = runTest{

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        dao.insertLocation(location)
        val res = localDataSource.deleteLocation(location)

        // Then (assert)
        assertThat(res, `is`(greaterThan(0)) )
    }
}