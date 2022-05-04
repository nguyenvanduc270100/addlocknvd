package com.lgi.applock.models

data class AppLockModel(
    var name: String,
    var describe: String,
    var image: String,
    var isFavorite: Boolean,
    var isLock: Boolean
)