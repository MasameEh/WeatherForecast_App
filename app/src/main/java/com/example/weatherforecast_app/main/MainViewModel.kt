package com.example.weatherforecast_app.main;


import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


private const val TAG = "MainViewModel"
class MainViewModel(private val repo: ILocationRepository) : ViewModel(){


    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

     fun checkNetworkStatus(context: Context) {
         Log.i(TAG, "checkNetworkStatus: ")
        val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val activeNetwork = connectivityManager.activeNetwork
         val isCurrentlyConnected = activeNetwork != null &&
                 connectivityManager.getNetworkCapabilities(activeNetwork) != null

         _isConnected.value = isCurrentlyConnected

         val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.i(TAG, "Internet Available")
                _isConnected.tryEmit(true)
            }

             // for active internet
             override fun onCapabilitiesChanged(
                 network: Network,
                 networkCapabilities: NetworkCapabilities
             ) {

             }
            override fun onLost(network: Network) {
                Log.i(TAG, "Internet Lost")
                _isConnected.tryEmit(false)
            }
        }
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
     fun getFreshLocation(onLocationReceived: (Location?) -> Unit){
        repo.getFreshLocation(onLocationReceived)
    }
}


class MainViewModelFactory(private val repo: ILocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}