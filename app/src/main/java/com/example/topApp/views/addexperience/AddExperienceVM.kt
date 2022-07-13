package com.example.topApp.views.addexperience

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
import com.example.topApp.views.modal.CategoryData
import com.example.topApp.views.modal.ExperienceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExperienceVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var networkErrorLiveData = MutableLiveData<String>()
    var errorLiveData = MutableLiveData<String>()
    var successsLiveData = MutableLiveData<Boolean>()
    var isProgressShow = ObservableBoolean(false)
    var errorMessage = ""
    var categoriesLiveData = MutableLiveData<List<CategoryData?>?>()
    val categoryList: ArrayList<CategoryData> = ArrayList()
    var years = ObservableField("")
    var area = ObservableField("")
    var categoryId = ""
    var categoryName = ""
    var uId = ""

    fun getCategoryData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            Utility.getUserData()?.let { userInfo ->
                userInfo.uid?.let {
                    uId = it
                }
            }

            categoryList.clear()
            firebaseUtils.fireStoreDatabase.collection("subcategories").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Utility.printLog("categories data", "documents ${document.data}")

                        categoryList.add(
                            CategoryData(
                                id = document.id, url = document.getString("url"),
                                title = document.getString("title")
                            )
                        )
                    }
                    categoriesLiveData.postValue(categoryList)
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("categories error", "Error getting documents $exception")
                }
        }

    }

    fun saveExperienceData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                networkErrorLiveData.postValue(Utility.networkError)
                return@launch
            }

            if (TextUtils.isEmpty(categoryName)) {
                errorMessage = resourceProvider.getString(R.string.select_category)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            if (TextUtils.isEmpty(years.get()?.trim())) {
                errorMessage = resourceProvider.getString(R.string.empty_year)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }
            if (TextUtils.isEmpty(area.get()?.trim())) {
                errorMessage = resourceProvider.getString(R.string.empty_area)
                errorLiveData.postValue(Utility.errorMessage)
                return@launch
            }

            getExperience()
        }
    }

    private fun getExperience() {
        viewModelScope.launch(Dispatchers.IO) {

            val experienceList: ArrayList<ExperienceResponse> = ArrayList()
            experienceList.clear()
            var isUpdate = false
            var documentId = ""

            firebaseUtils.fireStoreDatabase.collection("experience").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Utility.printLog("experience data", " ${document.id}")
                        if (document.data.isNotEmpty()) {
                            if (uId == document.getString("uid")) {
                                experienceList.add(
                                    ExperienceResponse(
                                        documentId = document.id,
                                        uid = uId,
                                        categoryId = document.getString("categoryId"),
                                        categoryName = document.getString("categoryName"),
                                        yearOfExperience = document.getString("yearOfExperience"),
                                        areaOfExpertise = document.getString("areaOfExpertise"),
                                    )
                                )
                            }
                        }
                    }

                    if (experienceList.isNotEmpty()) {
                        run {
                            experienceList.forEach {
                                if (it.categoryId == categoryId) {
                                    isUpdate = true
                                    documentId = it.documentId ?: ""
                                    return@run
                                }
                            }
                        }
                        if (isUpdate) {
                            updateExperience(documentId)
                        } else {
                            addExperience()
                        }
                    }else{
                        addExperience()
                    }
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("experience error", "Error getting documents $exception")
                    successsLiveData.postValue(false)
                }

        }
    }


    private fun addExperience() {
        viewModelScope.launch(Dispatchers.IO) {
            val experience = ExperienceResponse(
                uid = uId,
                categoryId = categoryId,
                categoryName = categoryName,
                yearOfExperience = years.get()?.trim(),
                areaOfExpertise = area.get()?.trim()
            )
            firebaseUtils.fireStoreDatabase.collection("experience").add(experience)
                .addOnSuccessListener { querySnapshot ->
                    successsLiveData.postValue(true)
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("experience error", "Error getting documents $exception")
                    successsLiveData.postValue(false)
                }
        }
    }

    private fun updateExperience(documentId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val experience = ExperienceResponse(
                documentId = documentId,
                uid = uId,
                categoryId = categoryId,
                categoryName = categoryName,
                yearOfExperience = years.get()?.trim(),
                areaOfExpertise = area.get()?.trim()
            )

            firebaseUtils.fireStoreDatabase.collection("experience").document(documentId).set(
                experience
            ).addOnSuccessListener { querySnapshot ->
                successsLiveData.postValue(true)
            }
                .addOnFailureListener { exception ->
                    Utility.printLog("experience error", "Error getting documents $exception")
                    successsLiveData.postValue(false)
                }

        }
    }

}