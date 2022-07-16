package com.example.topApp.views.profile.user

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var isProgressShow = ObservableBoolean(false)
    var errorLiveData = MutableLiveData<String>()
    var uid = ""
    var errorMessage = ""
    var nameField = ObservableField("")
    var emailField = ObservableField("")
    var cityField = ObservableField("")

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }

            Utility.getUserData()?.let { userInfo ->
                nameField.set(userInfo.firstName + " " + userInfo.lastName)
                emailField.set(userInfo.email)
                cityField.set(userInfo.city)
            }

        }
    }
}