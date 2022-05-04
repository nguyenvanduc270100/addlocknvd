package com.bienlongtuan.applocker

import android.content.Intent
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.bienlongtuan.applocker.utils.Preferences
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