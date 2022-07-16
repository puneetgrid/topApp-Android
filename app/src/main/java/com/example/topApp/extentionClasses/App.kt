package com.example.topApp.extentionClasses

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    /** FCM Token */
    var fcmToken: String? = null


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        topAppApplication = this

        /* Get FCM Token from Firebase Instance */
        FirebaseInstallations.getInstance().getToken(false)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                fcmToken = task.result?.token
            })

    }



    companion object {
        lateinit var topAppApplication: App
    }

}