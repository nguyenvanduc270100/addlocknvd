package com.lgi.applock.activities

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.patternlockview.utils.ResourceUtils
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent
import com.lgi.applock.R
import com.lgi.applock.databinding.ActivitySplashBinding
import com.lgi.applock.services.AppLockerService
import com.lgi.applock.services.BackgroundManager
import com.lgi.applock.utils.Constants
import com.lgi.applock.utils.preferences.LockPreferences
import io.reactivex.functions.Consumer
import javax.inject.Inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    @Inject
    lateinit var preferences: LockPreferences

    override fun setViewBinding(): ActivitySplashBinding {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        return ActivitySplashBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = LockPreferences();
        checkPermissions()
    }


    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this) && hasUsageStatsPermission()) {
                binding.llPer.visibility = View.GONE
                checkSetupPassword()
            } else {
                binding.llPattern.visibility = View.GONE
                binding.rlPin.visibility = View.GONE
                binding.llPer.visibility = View.VISIBLE
                binding.container.visibility = View.VISIBLE
                checkPermission()
            }
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
//        rl_draw
        binding.rlDraw.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + packageName)
                )
                startActivityForResult(intent, 1234)
            }
        }

        //rl_use
        binding.rlUse.setOnClickListener {
            if (!hasUsageStatsPermission()) {
                startActivityForResult(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    12345
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this) && hasUsageStatsPermission()) {
                binding.llPer.visibility = View.GONE
                checkSetupPassword()

            } else {
                binding.llPattern.visibility = View.GONE
                binding.rlPin.visibility = View.GONE
                binding.llPer.visibility = View.VISIBLE
                checkPermission()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun checkSetupPassword() {
        if (!BackgroundManager.isServiceRunning(AppLockerService::class.java, this)) {
            val serviceIntent = Intent(this, AppLockerService::class.java)
            serviceIntent.putExtra("inApp", "inApp")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(serviceIntent)
            } else {
                this.startService(serviceIntent)
            }
        }

        val intent = Intent(this@SplashActivity, SetPinActivity::class.java)
        intent.putExtra(Constants.SET_PIN_FIRST_TIME, preferences.password == "")
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)


    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun hasUsageStatsPermission(): Boolean {
        val appOps =
            getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }


}