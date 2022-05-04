package com.lgi.applock.models

import com.lgi.applock.utils.Preferences

data class TypeLock(
    var typeLock: TypeLockApp,
    var password: String
)

enum class TypeLockApp(val value: Int) {
    Unknown(0),
    Pin(1),
    Pattern(2);

    companion object {
        private val values = values()
        fun getByValue(value: Int?) = values.first { it.value == value }
    }
}