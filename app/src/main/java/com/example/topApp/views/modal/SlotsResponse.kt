package com.example.topApp.views.modal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class SlotsResponse(
    val categories: List<SlotsData>? = null,
) : Parcelable

@Parcelize
@Keep
data class SlotsData(
    val id: String? = null,
    val title: String? = null,
    var isSelected: Boolean = false
) : Parcelable
