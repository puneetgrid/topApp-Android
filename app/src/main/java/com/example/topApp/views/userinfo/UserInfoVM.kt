package com.example.topApp.views.userinfo

import android.text.TextUtils
import android.util.Patterns
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
class UserInfoVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils

) : BaseViewModel(networkLiveData) {
    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var email = ObservableField("")
    var errorLiveData = MutableLiveData<String>()
    var successsLiveData = MutableLiveData<Boolean>()
    var errorMessage = ""
    var uid = ""
    var isProgressShow = ObservableBoolean(false)

    fun createUSer() {
        viewModelScope.launch(Dispatchers.IO) {

            if (mNetworkErrorData.value?.status == false) {
                isProgressShow.set(false)
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            if (TextUtils.isEmpty(firstName.get()?.trim())){
                errorMessage = resourceProvider.getString(R.string.enter_first_name)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            } else  if (TextUtils.isEmpty(lastName.get()?.trim()))  {
                errorMessage = resourceProvider.getString(R.string.enter_last_name)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            else if (TextUtils.isEmpty(email.get()?.trim())) {
                errorMessage = resourceProvider.getString(R.string.enter_email)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.get()?.trim().toString()).matches()) {
                errorMessage = resourceProvider.getString(R.string.invalid_email)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            val user= UserInfoResponse(uid=uid,firstName=firstName.get()?.trim(),lastName=lastName.get()?.trim(),email=email.get()?.trim())
            firebaseUtils.fireStoreDatabase.collection("users").add(user)
                .addOnSuccessListener { querySnapshot ->
                    Utility.setUserData(user)
                    successsLiveData.postValue(true)
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("categories error", "Error getting documents $exception")
                }
        }
    }
}