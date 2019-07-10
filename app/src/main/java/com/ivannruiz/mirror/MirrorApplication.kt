package com.ivannruiz.mirror

import android.app.Application
import com.google.firebase.FirebaseApp

class MirrorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}