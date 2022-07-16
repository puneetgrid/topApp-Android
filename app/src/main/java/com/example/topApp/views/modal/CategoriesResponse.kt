package com.example.topApp.views.modal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class CategoriesResponse(
    val categories: List<CategoryData>? = null,
) : Parcelable

@Parcelize
@Keep
data class CategoryData(
    val id:String?=null,
    val url: String? = null,
    val title: String? = null,
    var isSelected:Boolean=false
) : Parcelable
