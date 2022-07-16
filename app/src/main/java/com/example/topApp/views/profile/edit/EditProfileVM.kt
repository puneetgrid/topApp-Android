package com.example.topApp.views.profile.edit

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
class EditProfileVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var city = ObservableField("")
    var isProgressShow = ObservableBoolean(false)
    var errorLiveData = MutableLiveData<String>()
    var errorMessage = ""

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }

            Utility.getUserData()?.let { userInfo ->
                city.set(userInfo.city)
            }

        }
    }
}