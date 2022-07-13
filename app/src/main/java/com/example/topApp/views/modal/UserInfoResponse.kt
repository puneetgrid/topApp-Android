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
    val city: String? = null,
    val callId: String? = null,
    val userType: String? = null,
    val loginTime: String? = null,
) : Parcelable
