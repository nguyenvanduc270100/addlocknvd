package com.lgi.applock.activities

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.provider.Settings
import android.util.Log
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
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finishAffinity()
            } else {
                binding.llPattern.visibility = View.GONE
                binding.rlPin.visibility = View.GONE
                binding.llPer.visibility = View.VISIBLE
                binding.container.visibility = View.VISIBLE
                checkPermission()
            }
        } else {
            setup()

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
                setup()
                binding.llPer.visibility = View.GONE

            } else {
                binding.llPattern.visibility = View.GONE
                binding.rlPin.visibility = View.GONE
                binding.llPer.visibility = View.VISIBLE
                checkPermission()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun setup() {
        if (preferences.pattern == "") {
            binding.container.visibility = View.VISIBLE
        } else {
            binding.container.visibility = View.GONE
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

        }

        if (preferences.pin == true) {
            binding.llPattern.visibility = View.GONE
            binding.rlPin.visibility = View.VISIBLE
            setUpPin()
        } else {
            binding.llPattern.visibility = View.VISIBLE
            binding.rlPin.visibility = View.GONE
            setUpPassword()
        }
    }

    @SuppressLint("CheckResult")
    private fun setUpPassword() {
        if (preferences.pattern != "") {
            binding.tvPass.text = "Enter Pattern"
        }

        binding.patterLockView.run {

            dotCount = 3
            dotNormalSize = ResourceUtils.getDimensionInPx(
                this@SplashActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_size
            ).toInt()
            dotSelectedSize = ResourceUtils.getDimensionInPx(
                this@SplashActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_selected_size
            ).toInt()
            pathWidth = ResourceUtils.getDimensionInPx(
                this@SplashActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_path_width
            ).toInt()
            isAspectRatioEnabled = true
            aspectRatio = PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
            setViewMode(PatternLockView.PatternViewMode.CORRECT)
            dotAnimationDuration = 150
            pathEndAnimationDuration = 100
            correctStateColor = ResourceUtils.getColor(
                this@SplashActivity,
                R.color.white
            )
            isInStealthMode = false
            isTactileFeedbackEnabled = true
            isInputEnabled = true
            addPatternLockListener(mPatternLockViewListener)
        }


        RxPatternLockView.patternComplete(binding.patterLockView)
            .subscribe(object : Consumer<PatternLockCompleteEvent> {
                @Throws(Exception::class)
                override fun accept(patternLockCompleteEvent: PatternLockCompleteEvent) {
                    Log.d(
                        javaClass.name,
                        "Complete: " + patternLockCompleteEvent.pattern.toString()
                    )
                }
            })

        RxPatternLockView.patternChanges(binding.patterLockView)
            .subscribe(object : Consumer<PatternLockCompoundEvent> {
                @Throws(Exception::class)
                override fun accept(event: PatternLockCompoundEvent) {
                    when (event.eventType) {
                        PatternLockCompoundEvent.EventType.PATTERN_STARTED -> {
                            Log.d(javaClass.name, "Pattern drawing started")
                        }
                        PatternLockCompoundEvent.EventType.PATTERN_PROGRESS -> {
                            Log.d(
                                javaClass.name, "Pattern progress: " +
                                        PatternLockUtils.patternToString(
                                            binding.patterLockView,
                                            event.pattern
                                        )
                            )
                        }
                        PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> {
                            Log.d(
                                javaClass.name, "Pattern complete: " +
                                        PatternLockUtils.patternToString(
                                            binding.patterLockView,
                                            event.pattern
                                        )
                            )
                        }
                        PatternLockCompoundEvent.EventType.PATTERN_CLEARED -> {
                            Log.d(javaClass.name, "Pattern has been cleared")
                        }
                    }
                }
            })
    }

    private fun setUpPin() {
        if (preferences.pattern != "") {
            binding.tvPassPin.text = "Enter Pin"
        }
        binding.pinLockViewPin.run {
            attachIndicatorDots(binding.indicatorDotsPin)
            setPinLockListener(mPinLockListener)
            pinLength = 4
            textColor = ContextCompat.getColor(this@SplashActivity, R.color.white)

        }
        binding.indicatorDotsPin.indicatorType = IndicatorDots.IndicatorType.FILL_WITH_ANIMATION

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun hasUsageStatsPermission(): Boolean {
        val appOps =
            getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private val mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            if (preferences.pattern == pin

            ) {

                binding.tvPassPin.visibility = View.GONE
                binding.tvPassPinAgainSplash.visibility = View.GONE
                binding.tvPassPin.text = resources.getString(R.string.enter_pin)
                preferences.pattern = pin
                binding.pinLockViewPin.resetPinLockView()
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()

            } else {
                if (preferences.pattern == "") {
                    preferences.pattern = pin
                    binding.tvPass.text = resources.getString(R.string.creat_pattern)
                    binding.tvPassPinAgainSplash.text = resources.getString(R.string.again)
                } else {

                    binding.tvPassPinWrong.visibility = View.VISIBLE
                    binding.tvPassPin.visibility = View.GONE
                    binding.tvPassPinAgainSplash.visibility = View.GONE
                }
                Handler().postDelayed({
                    binding.pinLockViewPin.resetPinLockView()
                }, 500)

            }
        }

        override fun onEmpty() {
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            binding.tvPassPinWrong.visibility = View.GONE
            binding.tvPassPin.visibility = View.VISIBLE
            binding.tvPassPinAgainSplash.visibility = View.VISIBLE
        }
    }

    private var mPatternLockViewListener = object : PatternLockViewListener {
        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {

            if (preferences.pattern == PatternLockUtils.patternToString(
                    binding.patterLockView,
                    pattern
                )
            ) {

                binding.tvPassPin.visibility = View.GONE
                binding.tvPassAgainSplash.visibility = View.GONE
                binding.tvPassPin.text = resources.getString(R.string.draw_pattern)
                preferences.pattern =
                    PatternLockUtils.patternToString(binding.patterLockView, pattern)
                binding.patterLockView.clearPattern()
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()

            } else {
                if (preferences.pattern == "") {
                    preferences.pattern =
                        PatternLockUtils.patternToString(binding.patterLockView, pattern)
                    binding.tvPassPin.text = resources.getString(R.string.creat_pattern)
                    binding.tvPassAgainSplash.text = resources.getString(R.string.again)
                } else {
                    binding.tvPassWrong.visibility = View.VISIBLE
                    binding.tvPassPin.visibility = View.GONE
                    binding.tvPass.visibility = View.GONE
                    binding.tvPassAgainSplash.visibility = View.GONE
                }
                Handler().postDelayed({
                    binding.patterLockView.clearPattern()
                }, 500)

            }

        }


        override fun onCleared() {
        }

        override fun onStarted() {
        }

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
            binding.tvPassWrong.visibility = View.GONE
            binding.tvPassPin.visibility = View.VISIBLE
            binding.tvPassAgainSplash.visibility = View.VISIBLE
        }

    }


}