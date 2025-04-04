package com.example.weatherforecast_app.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast_app.data.model.LocationInfo
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class LocationsDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: LocationsDao

    @Before
    fun setUp(){
        // different database from the actual database, live as long as the test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        dao = database.getLocationsDao()
    }
    @After
    fun tearDown() = database.close()

    @Test
    fun insertLocation_locationInfo_returnsGreaterThanZero() = runTest {

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        val res = dao.insertLocation(location)

        // Then (assert)
        assertThat(res, `is`(greaterThan(0)) )
    }

    @Test
    fun deleteLocation_locationInfo_returnsGreaterThanZero() = runTest{

        // Given (arrange)
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)

        // When (act)
        dao.insertLocation(location)
        val res = dao.deleteLocation(location)

        // Then (assert)
        assertThat(res, `is`(greaterThan(0)) )
    }

}