package com.example.topApp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.example.topApp.extentionClasses.App
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.views.modal.UserInfoResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson


class Utility {
    companion object {

        const val networkError = "networkError"
        const val errorMessage = "errorMessage"

        @JvmStatic
        fun showDialog(
            context: Context?,
            title: String?,
            message: String?,
            okayText: String?,
            cancellable: Boolean,
            dialogCallback: DialogCallback?
        ) {
            if (context == null) return
            try {
                val builder =
                    AlertDialog.Builder(
                        context
                    )
                builder.setTitle(title)
                builder.setMessage(message)
                builder.setPositiveButton(okayText) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    dialogCallback?.callback(0)
                }
                builder.setCancelable(cancellable)
                builder.show()
            } catch (ignore: Exception) {
            }
        }

        fun networkErrorDialog(
            context: Context?,
            title: String?,
            message: String?,
            positiveButtonText: String?,
            negativeButtonText: String?,
            cancellable: Boolean,
            dialogCallback: DialogCallback?
        ) {
            if (context == null) return
            try {
                val create = MaterialAlertDialogBuilder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(cancellable)
                    .setPositiveButton(positiveButtonText) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        dialogCallback?.callback(1)
                    }

                    .setNegativeButton(negativeButtonText) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        dialogCallback?.callback(0)
                    }
                    .create()
                create.show()
            } catch (ignore: Exception) { }
        }

        @JvmStatic
        fun printLog(message: String?, classNameAny: Any) {
            val callingClass =
                if (classNameAny is String) classNameAny else classNameAny.javaClass.simpleName
            Log.d(callingClass, "$message")
        }

        var preferences = "TopApp"
        val defaultPreferences: SharedPreferences
            get() = App.topAppApplication.getSharedPreferences(
                preferences,
                Context.MODE_PRIVATE
            )

        @JvmStatic
        val defaultPreferenceEditor: SharedPreferences.Editor
            get() = defaultPreferences.edit()


        const val loggedInUserData = "loggedInUserData"
        fun getUserData(): UserInfoResponse? {
            val eventData = defaultPreferences.getString(loggedInUserData, "")
            return if (TextUtils.isEmpty(eventData)) null else Gson().fromJson(
                eventData,
                UserInfoResponse::class.java
            )
        }

        fun setUserData(userData: UserInfoResponse) {
            defaultPreferenceEditor.putString(
                loggedInUserData, Gson().toJson(
                    userData,
                    UserInfoResponse::class.java
                )
            ).apply()
        }
    }
}