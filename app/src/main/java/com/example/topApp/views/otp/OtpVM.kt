package com.example.topApp.views.otp

import android.text.TextUtils
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
    var successLiveData = MutableLiveData<Boolean>()
    var userExistLiveData = MutableLiveData<Boolean>()
    var verificationId = ""
    var phone = ""
    var uid = ""
    var loginTime = 0L
    var isProgressShow = ObservableBoolean(false)
    var errorMessage = ""

    fun checkOtp() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            if (TextUtils.isEmpty(otpField.get()?.trim())) {
                errorMessage = resourceProvider.getString(R.string.invalid_otp)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            successLiveData.postValue(true)
        }
    }

    fun checkUser() {
        viewModelScope.launch(Dispatchers.IO) {
            var isUserExist = false

            var user = UserInfoResponse(
                uid = uid
            )
            firebaseUtils.fireStoreDatabase.collection("users").get()
                .addOnSuccessListener { querySnapshot ->
                    run {
                        querySnapshot.forEach { document ->
                            Utility.printLog("users data", "documents ${document.data}")
                            if (document.data.isEmpty()) {
                                isUserExist = false
                            } else {
                                if (document.getString("uid") == uid) {
                                    user = user.copy(
                                        firstName = document.getString("firstName"),
                                        lastName = document.getString("lastName"),
                                        email = document.getString("email"),
                                        city = document.getString("city"),
                                        callId = document.getString("callId"),
                                        userType = document.getString("userType"),
                                        loginTime = document.getString("loginTime"),
                                    )
                                    isUserExist = true
                                    return@run
                                }
                            }
                        }
                    }

                    Utility.setUserData(user)
                    userExistLiveData.postValue(isUserExist)
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("users error", "Error getting documents $exception")
                }
        }
    }
}