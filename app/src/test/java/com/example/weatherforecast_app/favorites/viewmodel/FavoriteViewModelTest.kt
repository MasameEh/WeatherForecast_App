package com.example.weatherforecast_app.favorites.viewmodel

import androidx.compose.runtime.collectAsState
import app.cash.turbine.test
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.example.weatherforecast_app.utils.ResponseState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest{
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var repo: ILocationRepository
    private lateinit var testData: List<LocationInfo>

    @Before
    fun setUp() {
        repo = mockk(relaxed = true)
        viewModel = FavoriteViewModel(repo)


        Dispatchers.setMain(StandardTestDispatcher())

        testData = listOf(
            LocationInfo("Egypt", "Cairo", 30.0444, 31.2357),
            LocationInfo("Egypt", "Zagazig", 30.112, 31.44),
            LocationInfo("USA", "New York", 40.7128, -74.0060)
        )
        coEvery {
            repo.getAllFavLocations()
        } returns flowOf(
            testData
        )
    }

    @After
    fun teardown() = Dispatchers.resetMain()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavLocation_locationInfo_emitsSuccessMessage() = runTest {

        // Given
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)
        coEvery { repo.deleteLocation(any()) } returns 1

        // When
        viewModel.deleteLocationFromFav(location)

        advanceUntilIdle()
        // Then
        assertEquals(viewModel.message, "Deleted from Favorite Locations successfully")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavLocation_locationInfo_emitsFailureMessage() = runTest {

        // Given
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)
        coEvery { repo.deleteLocation(any()) } returns 0

        // When
        viewModel.deleteLocationFromFav(location)
        val msg: String = viewModel.message.first()

        advanceUntilIdle()
        // Then
        assertEquals(msg, "Error!! Couldn't be deleted, try again")
    }


    @Test
    fun getFavLocations_initialStateShouldBeLoading() = runTest {
        val initialState = viewModel.favoritesList.value
        assertTrue(initialState is ResponseState.Loading)
    }


    @Test
    fun getFavLocations_noUpdate_emitsResponseSuccessWithList() = runTest  {

        // Given

        // When
        viewModel.getAllFavorites()

        //Then
        viewModel.favoritesList.test {
            assertThat(awaitItem(), `is`(ResponseState.Success(testData)))
            cancelAndIgnoreRemainingEvents()
        }
    }
}