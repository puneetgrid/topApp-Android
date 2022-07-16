package com.example.topApp.views.home

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
import com.example.topApp.views.modal.CategoryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var isProgressShow = ObservableBoolean(false)
    var errorLiveData = MutableLiveData<String>()
    var categoriesLiveData = MutableLiveData<List<CategoryData?>?>()
    var userName = ObservableField("")

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            val categoryList: ArrayList<CategoryData> = ArrayList()
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

    fun getUserData() {
        Utility.getUserData()?.let { userInfo ->
            userInfo.uid?.let {
                userName.set("Welcome "+userInfo.firstName +" "+ userInfo.lastName)
            }
        }
    }
}