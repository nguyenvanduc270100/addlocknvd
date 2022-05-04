package com.bienlongtuan.applocker.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.bienlongtuan.applocker.values.Constants

object BackgroundUtils {
    fun saveBackground(path: String, activity: Activity) {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.backgroundImage,
            Context.MODE_PRIVATE
        )
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putString(Constants.SHARE_PREF.backgroundImage, path)
        sharedPreferencesEditor.apply()
        sharedPreferencesEditor.commit()
    }

    fun getBackground (activity: Activity): String {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.backgroundImage,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(Constants.SHARE_PREF.backgroundImage,"")!!
    }
}