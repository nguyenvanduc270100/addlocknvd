package com.lgi.applock.utils.preferences

import com.lgi.applock.models.TypeLock
import com.lgi.applock.models.TypeLockApp
import com.lgi.applock.utils.Preferences
import com.lgi.applock.values.Constants.SHARE_PREF.pinCode
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LockPreferences @Inject constructor() : Preferences() {
    var pattern by stringPref("pattern","")
    var pin by booleanPref("pin", false)
}