package com.example.topApp.views.modal


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DayDataClass(
    val dayTitle: String,
    val dayNumber: String,
    var isSelected: Boolean = false,
) : Parcelable

