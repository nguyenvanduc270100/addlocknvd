package com.bienlongtuan.applocker.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.bienlongtuan.applocker.values.Constants.SHARE_PREF.pinCode

object PinEncoderUtils {
    fun checkIfPinCodeExisted(activity: Activity): Boolean {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            pinCode,
            Context.MODE_PRIVATE
        )
        val currentCode = sharedPreferences.getString(pinCode, "")
        if (currentCode == "") {
            return false
        }
        return true
    }

    fun savePinCode(PIN: String, activity: Activity) {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences(
            pinCode,
            Context.MODE_PRIVATE
        )
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putString(pinCode,PIN)
        sharedPreferencesEditor.apply()
        sharedPreferencesEditor.commit()
    }

    fun pseudoCodeToId(code: String?): Long? {
        var base: Long = 1
        var id: Long = 0
        if (code == null || code.length != 10) {
            return null
        }
        for (i in 0 until code.length) {
            id += base * codeTable[i].indexOf(code[i])
            base *= numberOfCharacter.toLong()
        }
        return id
    }

    private val numberOfCharacter = 36
    private val codeTable = arrayOf(
        "m0vde7lnowxyz89abcpqrstufghijk123456",
        "zyxwvutsrqponmlkjihgfedcba9876543210",
        "01234567lmnopqrstuvwxyz89abcdefghijk",
        "kjihgfedcba98zyxwvutsrqponml76543210",
        "pqrstuvwxyz89abcdefghijk1234567lm0no",
        "on0ml7654321kjihgfedcba98zyxwvutsrqp",
        "pqrstuvdefghijk1234567lm0nowxyz89abc",
        "cba98zyxwon0ml7654321kjihgfedvutsrqp",
        "m0nowxyz89abcpqrstuvdefghijk1234567l",
        "l7654321kjihgfedvutsrqpcba98zyxwon0m",
        "m0vdefghijk1234567lnowxyz89abcpqrstu",
        "utsrqpcba98zyxwonl7654321kjihgfedv0m"
    )
}