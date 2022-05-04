package com.lgi.applock

import androidx.multidex.MultiDexApplication
import com.lgi.applock.utils.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {
    companion object {
        lateinit var instance: App private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Preferences.init(this)
    }
}