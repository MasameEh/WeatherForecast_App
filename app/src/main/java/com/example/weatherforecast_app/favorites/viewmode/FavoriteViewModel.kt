package com.example.weatherforecast_app.favorites.viewmode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.repo.ILocationRepository
import com.example.weatherforecast_app.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private const val TAG = "FavoriteViewModel"
class FavoriteViewModel(
    private val repo: ILocationRepository
): ViewModel() {

    private val _mutableFavoritesList : MutableStateFlow<ResponseState> = MutableStateFlow(ResponseState.Loading)
    val favoritesList: StateFlow<ResponseState> = _mutableFavoritesList.asStateFlow()

    private val mutableMsg: MutableSharedFlow<String> = MutableSharedFlow()
    val message = mutableMsg.asSharedFlow()

    fun getAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAllFavLocations()
            result.catch { ex ->
                mutableMsg.emit("Error!! Couldn't be retrieved, try again")
                _mutableFavoritesList.value = ResponseState.Failure(ex)
            }.collect{
                _mutableFavoritesList.value = ResponseState.Success(it)
            }
        }

    }

    fun deleteLocationFromFav(location : LocationInfo){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result  = repo.deleteLocation(location)
                if(result > 0){
                    mutableMsg.emit("Deleted from Favorite Locations successfully")
                }else{
                    mutableMsg.emit("Error!! Couldn't be deleted, try again")
                    Log.i(TAG, "deleteLocationFromFav: res = $result")
                }
            }catch (ex: Exception){
                Log.i(TAG, "deleteLocationFromFav: ex = $ex")
                mutableMsg.emit("Error!! Couldn't be added, try again")
            }

        }

    }

}


class  FavoriteViewModelFactory(private val repo: ILocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(repo) as T
    }
}