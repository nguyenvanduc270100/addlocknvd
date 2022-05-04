package com.bienlongtuan.applocker.data.model

import androidx.room.Entity

@Entity
data class DBAppLock(
    var name: String,
    var packagename: String,
    var version: String,
    var isLock: Int
)