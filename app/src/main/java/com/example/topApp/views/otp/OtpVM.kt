package com.example.topApp.views.otp

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
import com.example.topApp.views.modal.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var otpField = ObservableField("")
    var errorLiveData = MutableLiveData<String>()
    var successsLiveData = MutableLiveData<Boolean>()
    var userExistLiveData = MutableLiveData<Boolean>()
    var verificationId = ""
    var phone = ""
    var uid = ""
    var isProgressShow = ObservableBoolean(false)
    var errorMessage = ""

    fun checkOtp() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            if (otpField.get()?.trim().isNullOrEmpty()) {
                errorMessage = resourceProvider.getString(R.string.invalid_otp)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            successsLiveData.postValue(true)
        }
    }

    fun checkUser() {
        userExistLiveData.postValue(false)
        firebaseUtils.fireStoreDatabase.collection("users").get()
            .addOnSuccessListener { querySnapshot ->
                run {
                    querySnapshot.forEach { document ->
                        Utility.printLog("users data", "documents ${document.data}")
                        if (document.getString("uid") == uid) {

                            val user = UserInfoResponse(
                                uid = uid,
                                firstName = document.getString("firstName"),
                                lastName = document.getString("lastName"),
                                email = document.getString("email")
                            )
                            Utility.setUserData(user)
                            userExistLiveData.postValue(true)
                            return@run
                        }
                    }
                }

            }
            .addOnFailureListener { exception ->
                Utility.printLog("categories error", "Error getting documents $exception")
            }
    }

}