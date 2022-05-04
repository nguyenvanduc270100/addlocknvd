package com.lgi.applock.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.lgi.applock.values.Constants

object ThemeUtils {
    fun saveTheme(position: Int, strColor: String, strKeyboard: String, activity: Activity) {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putInt(Constants.SHARE_PREF.COLOR_POSITION, position)
        sharedPreferencesEditor.putString(Constants.SHARE_PREF.COLOR, strColor)
        sharedPreferencesEditor.putString(Constants.SHARE_PREF.COLOR_KEYBOARD, strKeyboard)
        sharedPreferencesEditor.apply()
        sharedPreferencesEditor.commit()
    }


    fun getTheme(activity: Activity): Int {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getInt(Constants.SHARE_PREF.COLOR_POSITION, 7)
    }

    fun getColor(activity: Activity): String {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(Constants.SHARE_PREF.COLOR, "#0A90DC")!!
    }

    fun getColorKeyboard(activity: Activity): String {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.sharedPrefFile,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(Constants.SHARE_PREF.COLOR_KEYBOARD, "#FFFFFF")!!
    }

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

    fun getBackground(activity: Activity): String {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            Constants.SHARE_PREF.backgroundImage,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(Constants.SHARE_PREF.backgroundImage, "")!!
    }
}