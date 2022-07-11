package com.example.topApp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import com.example.topApp.extentionClasses.App
import javax.inject.Inject

enum class ConnectivityState {
    WIFI_CONNECTED,
    MOBILE_DATA_CONNECTED,
    DISCONNECTED
}

object NetworkMessage {
    const val NETWORK_CONNECTED = "Connected to Network"
    const val NO_NETWORK = "No Network Available. Please connect to WiFi or Mobile Data."
}

/**
 * To Detect Network Changes
 * */
class NetworkLiveData @Inject constructor() : LiveData<ConnectivityState>() {
    val context: Context = App.topAppApplication.applicationContext

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    /**
     * Check Network is connected or not
     * */
    val isNetworkConnected : Boolean
        get() = when(getConnectivityState()) {
            ConnectivityState.WIFI_CONNECTED, ConnectivityState.MOBILE_DATA_CONNECTED -> true
            else -> false
        }

    /**
     * CHECK INTERNET CONNECTION STATUS
     */
    fun getConnectivityState(): ConnectivityState {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            checkNetworkState(network)
        } else {
            checkNetworkState()
        }
    }

    /**
     * Network Callback
     * */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(ConnectivityState.DISCONNECTED)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(checkNetworkState(network))
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(ConnectivityState.DISCONNECTED)
        }
    }

    /**
     * Check Network Type
     * */
    private fun checkNetworkState(network: Network? = null): ConnectivityState {
        if (network != null) {
            val actNw = connectivityManager.getNetworkCapabilities(network)

            if (actNw != null) {
                if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return ConnectivityState.WIFI_CONNECTED
                }

                if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return ConnectivityState.MOBILE_DATA_CONNECTED
                }

            }

        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected) {
                return ConnectivityState.MOBILE_DATA_CONNECTED
            }
        }

        return ConnectivityState.DISCONNECTED
    }

    /**
     * LiveData: Callbacks - START
     * */
    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder().build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
    /** LiveData: Callbacks - END */



}