package com.bienlongtuan.applocker.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BroadCastBoot: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != null) {
            if (intent.action.equals(Intent.ACTION_USER_PRESENT)) {
                Log.e("e","BroadcastReceiver")
            }
        }
    }
}