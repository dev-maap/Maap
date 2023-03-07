package com.dev.maap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MaapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqliteX")
    }
}