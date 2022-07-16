package com.example.topApp.views.login

import android.text.TextUtils
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.topApp.R
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository
) : BaseViewModel(networkLiveData) {
    var phone = ObservableField("")
    var errorLiveData = MutableLiveData<String>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorMessage = ""
    var isProgressShow = ObservableBoolean(false)

    fun getOtp() {
        viewModelScope.launch(Dispatchers.IO) {

            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            if (TextUtils.isEmpty(phone.get()?.trim())){
                errorMessage = resourceProvider.getString(R.string.empty_phone_number)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            else if (phone.get()?.trim()?.length!=10){
                errorMessage = resourceProvider.getString(R.string.invalid_phone_number)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            successLiveData.postValue(true)
        }
    }

}