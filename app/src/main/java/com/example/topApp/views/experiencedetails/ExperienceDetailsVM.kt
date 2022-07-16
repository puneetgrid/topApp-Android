package com.example.topApp.views.experiencedetails

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseViewModel
import com.example.topApp.views.modal.ExperienceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceDetailsVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    private val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var isProgressShow = ObservableBoolean(false)
    var uid = ""
    var networkErrorLiveData = MutableLiveData<String>()
    var errorLiveData = MutableLiveData<String>()
    var errorMessage = ""
    var experienceLiveData = MutableLiveData<List<ExperienceResponse?>?>()
    var isAddMoreShow = ObservableBoolean(false)

    fun getExperience() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                networkErrorLiveData.postValue(Utility.networkError)
                return@launch
            }

            Utility.getUserData()?.let { userInfo ->
                userInfo.uid?.let {
                    uid = it
                }
            }

            val experienceList: ArrayList<ExperienceResponse> = ArrayList()
            experienceList.clear()
            firebaseUtils.fireStoreDatabase.collection("experience").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Utility.printLog("experience data", "documents ${document.data}")
                        if (document.data.isNotEmpty()) {
                            if (uid == document.getString("uid")) {
                                experienceList.add(
                                    ExperienceResponse(
                                        uid = uid,
                                        categoryId = document.getString("categoryId"),
                                        categoryName = document.getString("categoryName"),
                                        yearOfExperience = document.getString("yearOfExperience"),
                                        areaOfExpertise = document.getString("areaOfExpertise"),
                                    )
                                )
                            }
                        }
                        experienceLiveData.postValue(experienceList)
                    }
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("experience error", "Error getting documents $exception")
                }
        }
    }

}