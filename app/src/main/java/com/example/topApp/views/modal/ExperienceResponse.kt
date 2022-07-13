package com.example.topApp.views.modal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ExperienceResponse(
    val documentId:String?=null,
    val uid:String?=null,
    val categoryId: String? = null,
    val categoryName: String? = null,
    val yearOfExperience: String? = null,
    val areaOfExpertise: String? = null
) : Parcelable

