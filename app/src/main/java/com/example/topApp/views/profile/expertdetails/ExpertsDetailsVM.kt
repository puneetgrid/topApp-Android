package com.example.topApp.views.profile.expertdetails

import androidx.databinding.ObservableBoolean
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.views.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpertsDetailsVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {

    var isProgressShow = ObservableBoolean(false)
}