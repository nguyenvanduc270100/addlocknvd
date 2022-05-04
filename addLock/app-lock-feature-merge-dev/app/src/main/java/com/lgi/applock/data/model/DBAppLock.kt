package com.lgi.applock.data.model



data class DBAppLock(
    var name: String,
    var packagename: String,
    var flags: Int,
    var isLock: Int,
    var isFavorite: Int
)