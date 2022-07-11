package com.example.topApp.views.base


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class BaseModel(
     val status: Boolean = false,
    val message: String = "",

    /**
     * Because this class is also used as Error Model
     * So retryRequire param is used to identity that
     * error can be fix with retry or not.
     *
     * the para will not set from any api
     * */
    val retryRequire: Boolean = true
) : Parcelable {
    override fun toString(): String = "${javaClass.simpleName} : STATUS: $status MESSAGE: $message"

}


