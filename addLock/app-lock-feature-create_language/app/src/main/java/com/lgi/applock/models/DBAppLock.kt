package com.lgi.applock.models



data class DBAppLock(
    var name: String,
    var packagename: String,
    var flags: Int,
    var isLock: Int,
    var isFavorite: Int
)