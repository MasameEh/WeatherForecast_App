package com.example.weatherforecast_app.favorites.viewmodel

import androidx.compose.runtime.collectAsState
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.example.weatherforecast_app.utils.ResponseState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class FavoriteViewModelTest{
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var repo: ILocationRepository

    @Before
    fun setUp(){
        repo = mockk(relaxed = true)
        viewModel = FavoriteViewModel(repo)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavLocation_locationInfo_emitsSuccessMessage() = runTest {

        // Given
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)
        coEvery { repo.deleteLocation(any()) } returns 1

        // When
        viewModel.deleteLocationFromFav(location)
        advanceUntilIdle()

        val msg: String = viewModel.message.first()
        // Then
        assertEquals(msg, "Deleted from Favorite Locations successfully")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavLocation_locationInfo_emitsFailureMessage() = runTest {

        // Given
        val location = LocationInfo("Egypt", "Zagazig", 30.112, 31.44)
        coEvery { repo.deleteLocation(any()) } returns 0

        // When
        viewModel.deleteLocationFromFav(location)
        advanceUntilIdle()
        val msg: String = viewModel.message.first()

        // Then
        assertEquals(msg, "Error!! Couldn't be deleted, try again")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavLocations_noUpdate_emitsResponseSuccessWithList() = runTest {

        // Given

        // When
        viewModel.getAllFavorites()

        advanceUntilIdle()

        val res = viewModel.favoritesList.last()

        assertThat(res, `is`(ResponseState.Success(listOf(
            LocationInfo("Egypt", "Cairo", 30.0444, 31.2357),
            LocationInfo("Egypt", "Zagazig", 30.112, 31.44),
            LocationInfo("USA", "New York", 40.7128, -74.0060)
        ))))
    }
}