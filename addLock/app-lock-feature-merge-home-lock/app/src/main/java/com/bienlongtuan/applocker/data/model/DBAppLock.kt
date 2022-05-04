package com.bienlongtuan.applocker.data.model



data class DBAppLock(
    var name: String,
    var packagename: String,
    var version: String,
    var isLock: Int,
    var isFavorite: Int
)