package com.bienlongtuan.applocker.utils.preferences

import com.bienlongtuan.applocker.utils.Preferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LockPreferences @Inject constructor() : Preferences() {
    var pattern by stringPref("pattern","")
    var pin by booleanPref("pin", false)
}