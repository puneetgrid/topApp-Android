package com.example.topApp.views.modal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class QuestionResponse(
    val questions: List<QuestionData>? = null,
) : Parcelable

@Parcelize
@Keep
data class QuestionData(
    val id:String?=null,
    val title: String? = null,
    val answer1: String? = null,
    val answer2: String? = null,
) : Parcelable
