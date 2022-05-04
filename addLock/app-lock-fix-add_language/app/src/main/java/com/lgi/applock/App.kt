package com.lgi.applock

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.multidex.MultiDexApplication
import com.lgi.applock.services.AppLockerService
import com.lgi.applock.utils.Preferences
import com.lgi.applock.utils.preferences.LockPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : MultiDexApplication(), LifecycleObserver {
    @Inject lateinit var preferences: LockPreferences
    companion object {
        lateinit var instance: App private set
        var language = "en"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Preferences.init(this)
        language = preferences.language.toString()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {

    }

}