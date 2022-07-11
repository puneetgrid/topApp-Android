package com.example.topApp.utils

import androidx.lifecycle.MutableLiveData
import com.example.topApp.views.modal.CategoryData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUtils @Inject constructor() {
    val fireStoreDatabase = FirebaseFirestore.getInstance()

}