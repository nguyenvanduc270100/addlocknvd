package com.lgi.applock.services

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lgi.applock.R
import com.lgi.applock.key.KeyApp
import com.lgi.applock.key.KeyLock

object BackgroundManager {
    fun isServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name==service.service.className) {
                return true
            }
        }
        return false
    }

    fun startService(
        serviceClass: Class<*>,
        context: Context,
        activity: AppCompatActivity? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundServices(serviceClass, context)
        } else context.startService(Intent(context, serviceClass))
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startForegroundServices(serviceClass: Class<*>, context: Context) {
        context.startForegroundService(Intent(context, serviceClass))
    }

    fun requestPermission(
        activity: AppCompatActivity,
        isCreate: Boolean = true,
    ) {
        if (!isServiceRunning(AppLockerService::class.java, activity)) {
            startService(AppLockerService::class.java, activity, activity)
        }
    }


    fun startServiceAndUsageData(
        activity: AppCompatActivity,
        isCreate: Boolean = true,
        isDetailNotify: Boolean = false
    ) {
        if (!isServiceRunning(AppLockerService::class.java, activity)) {
            startService(AppLockerService::class.java, activity, activity)
        } else if (!isAccessGranted(activity)) {
            openUsageStats(activity)
        }
        if (!isCreate) {
            val intent = Intent(activity, AppLockerService::class.java)
            intent.putExtra(KeyApp.LOCK_MY_APP, true)
            intent.putExtra("KEY_DETAIL", isDetailNotify)
            intent.putExtra(
                KeyLock.PKG_APP, "com.ezteam.applocker"
            )
            activity.startActivity(intent)
            activity.finish()
        }
    }

    fun isAccessGranted(context: Context): Boolean {
        return try {
            val packageManager: PackageManager = context.packageManager
            val applicationInfo: ApplicationInfo =
                packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager: AppOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0
            mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid,
                applicationInfo.packageName
            )
            mode== AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun openUsageStats(context: Context) {
        val intent = Intent("android.settings.USAGE_ACCESS_SETTINGS")
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("package:" + context.packageName)
        try {
            context.startActivity(intent)
        } catch (unused: Exception) {
            intent.data = Uri.fromParts("package", context.packageName, null)
            try {
                context.startActivity(intent)
            } catch (unused2: Exception) {
                try {
                    intent.data = null
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "fail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}