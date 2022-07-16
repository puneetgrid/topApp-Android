package com.example.topApp.views.modal


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MonthPickerDataClass(
    val month: Int,
    val monthName: String,
    var isSelected: Boolean = false,
) : Parcelable

