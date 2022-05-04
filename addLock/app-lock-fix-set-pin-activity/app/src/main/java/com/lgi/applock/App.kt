package com.lgi.applock

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.multidex.MultiDexApplication
import com.lgi.applock.services.AppLockerService
import com.lgi.applock.utils.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication(), LifecycleObserver {
    companion object {
        lateinit var instance: App private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Preferences.init(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {

    }

}