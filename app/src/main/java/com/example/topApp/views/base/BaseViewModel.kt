package com.example.topApp.views.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topApp.utils.ConnectivityState
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.NetworkMessage
import com.google.firebase.auth.FirebaseAuth

abstract class BaseViewModel(
    networkLiveData: NetworkLiveData,
) : ViewModel() {

    /**
     * Network Connection/Disconnect Detection
     * */
    val mNetworkErrorData = MutableLiveData<BaseModel>()
    var mAuth: FirebaseAuth? = null

    init {
        /* Observer ConnectivityState Change */
        networkLiveData.observeForever {
            it?.let { onNetworkState(it) }
        }

        /* Need to Notify First Time
        *  Because NetworkLiveData doesn't send callback if network is disconnected
        *  while launching the app */
        networkLiveData.getConnectivityState().let {
            if (it == ConnectivityState.DISCONNECTED)
                onNetworkState(it)
        }
        mAuth = FirebaseAuth.getInstance()
    }

    /**
     * Update LiveData when State change
     * */
    private fun onNetworkState(connectivityState: ConnectivityState) {
        when (connectivityState) {
            ConnectivityState.WIFI_CONNECTED,
            ConnectivityState.MOBILE_DATA_CONNECTED -> {
                mNetworkErrorData.postValue(
                    BaseModel(
                        true,
                        NetworkMessage.NETWORK_CONNECTED
                    )
                )
            }

            ConnectivityState.DISCONNECTED -> {
                mNetworkErrorData.postValue(BaseModel(false, message = NetworkMessage.NO_NETWORK))
            }
        }
    }
}