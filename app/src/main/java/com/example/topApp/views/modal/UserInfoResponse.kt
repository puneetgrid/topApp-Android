package com.example.topApp.views.modal

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class UserInfoResponse(
    val uid:String?=null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
) : Parcelable
