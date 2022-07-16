package com.example.topApp.views.activity

import androidx.lifecycle.ViewModel
import com.example.topApp.extentionClasses.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    val resourceProvider: ResourceProvider
) : ViewModel() {
}