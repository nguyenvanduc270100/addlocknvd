package com.lgi.applock.models

data class UpdateEvent(
    val isUpdate: TypeActionUpdate,
    val model: DBAppLock? = null,
    val updateSwitch: Boolean? = false
)

data class LockState(val isUnLock: Boolean)

enum class TypeActionUpdate(val value: Int) {
    Favorite(0),
    Lock(1),
    UnFavorite(3);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }
}