package com.lgi.applock.windownmanager

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.patternlockview.utils.ResourceUtils
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lgi.applock.R
import com.lgi.applock.activities.SetPinActivity
import com.lgi.applock.databinding.ActivityScreenLockBinding
import com.lgi.applock.databinding.ActivitySetPinBinding
import com.lgi.applock.databinding.FragmentBlankBinding
import com.lgi.applock.databinding.FragmentToolBinding
import com.lgi.applock.layouts.PinInputKeyboard
import com.lgi.applock.models.LockState
import com.lgi.applock.utils.Constants
import com.lgi.applock.utils.ThemeUtils
import com.lgi.applock.utils.preferences.LockPreferences
import com.poovam.pinedittextfield.PinField
import com.shuhart.stepview.StepView
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus

class MyWindowLockScreen(var context: Context) : PinField.OnTextCompleteListener {
    private lateinit var binding: ActivitySetPinBinding
    private lateinit var windowManager: WindowManager
    private var pinCode = ""
    private var finalCode = ""
    private var currentStep = 0
    private var isFirstInit = false
    private var preferences: LockPreferences = LockPreferences()

    init {
        initView()
//        setupFunction()
    }

    private fun initView() {
        val layoutInflater =
            context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        binding = FragmentToolBinding.bind(
//            layoutInflater.inflate(
//                R.layout.fragment_tool,
//                null
//            )
//        )

        binding =
            ActivitySetPinBinding.inflate(LayoutInflater.from(context), LinearLayout(context), false)
//        //set up theme
//        initLockScreen()
//        initTheme()
        windowManager.addView(binding.root.rootView, setupLayout())

    }

    fun showLock() {
    }


    private fun setupLayout(): WindowManager.LayoutParams {
        val mLayoutParams: WindowManager.LayoutParams
        val flag =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        val height = getHeight()
        mLayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, height, type, flag, PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.alpha = 1F //
        return mLayoutParams
    }

    private fun getHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        return if (getNavigationBarSize(context).y > 0) {
            height + (getNavigationBarSize(context).y)
        } else {
            WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    private fun getNavigationBarSize(context: Context): Point {
        val appUsableSize = getAppUsableScreenSize(context)
        val realScreenSize = getRealScreenSize(context)

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return Point(realScreenSize.x - appUsableSize.x, appUsableSize.y)
        }
        // navigation bar at the bottom
        return if (appUsableSize.y < realScreenSize.y) {
            Point(appUsableSize.x, realScreenSize.y - appUsableSize.y)
        } else Point()
        // navigation bar is not present
    }

    private fun getAppUsableScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    private fun getRealScreenSize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        return size
    }

    fun clearView() {
        try {
            windowManager.removeView(binding.root.rootView)
        } catch (e: IllegalArgumentException) {
        }
    }

    override fun onTextComplete(enteredText: String): Boolean {
        return true
    }
//
//    private fun switchPinLockType() {
//        if (preferences.lockPin == false) {
//            preferences.lockPin = true
//            showPinLock()
//        } else {
//            preferences.lockPin = false
//            showPatternLock()
//        }
//    }
//
//    private fun initTheme() {
//        binding.loutPinInputKeyboard.changeKeyboardColor(
//            Color.parseColor(
//                ThemeUtils.getColor(
//                    context
//                )
//            )
//        )
//        binding.loutPinInputKeyboard.changeKeyboardCharacterColor(
//            Color.parseColor(
//                ThemeUtils.getColorKeyboard(
//                    context
//                )
//            )
//        )
//        binding.inputField.fieldBgColor = Color.parseColor(ThemeUtils.getColor(context))
//        binding.loutPinInputPattern.normalStateColor =
//            Color.parseColor(ThemeUtils.getColor(context))
//
//        var requestOption = RequestOptions()
//        requestOption = requestOption.centerInside()
//            .transform(CenterInside(), GranularRoundedCorners(30f, 30f, 0f, 0f))
//        Glide.with(context).load(ThemeUtils.getBackground(context)).centerCrop()
//            .apply(requestOption)
//            .into(object : CustomTarget<Drawable?>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable?>?
//                ) {
//                    binding.background.background = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    binding.background.background = placeholder
//                }
//
//            })
//    }
//
//
//    private fun initLockScreen() {
//        if (preferences.lockPin == true) {
//            showPinLock()
//        } else {
//            showPatternLock()
//        }
//    }
//
//    private fun showPinLock() {
//        binding.loutPinInputPattern.visibility = View.GONE
//        binding.loutPinInputKeyboard.visibility = View.VISIBLE
//        binding.inputField.visibility = View.VISIBLE
//    }
//
//    private fun showPatternLock() {
//        binding.loutPinInputPattern.visibility = View.VISIBLE
//        binding.loutPinInputKeyboard.visibility = View.GONE
//        binding.inputField.visibility = View.GONE
//    }
//
//
//    private fun setupFunction() {
//        binding.inputField.showSoftInputOnFocus = false
//        binding.loutPinInputKeyboard.onPressPinInputKeyListener = object :
//            PinInputKeyboard.OnPressPinInputKeyListener {
//            override fun onClickKey(keyString: String) {
//                if (binding.inputField.text.toString().length < 4) {
//                    pinCode += keyString
//                    binding.inputField.setText(pinCode)
//                }
//            }
//
//            override fun onClickDelete() {
//                if (pinCode == "") {
//                    return
//                } else {
//                    pinCode = pinCode.substring(0, pinCode.length - 1)
//                }
//                binding.inputField.setText(pinCode)
//            }
//        }
//        binding.inputField.onTextCompleteListener = this
//        binding.step.state.animationType(StepView.ANIMATION_LINE).stepsNumber(2)
//            .animationDuration(context.resources.getInteger(android.R.integer.config_shortAnimTime))
//            .commit()
//
//        setupPattern()
//    }
//
//
//    @SuppressLint("CheckResult")
//    private fun setupPattern() {
//
//        RxPatternLockView.patternComplete(binding.loutPinInputPattern)
//            .subscribe(object : Consumer<PatternLockCompleteEvent> {
//                @Throws(Exception::class)
//                override fun accept(patternLockCompleteEvent: PatternLockCompleteEvent) {
//                    Log.d(
//                        javaClass.name,
//                        "Complete: " + patternLockCompleteEvent.pattern.toString()
//                    )
//                }
//            })
//
//        RxPatternLockView.patternChanges(binding.loutPinInputPattern)
//            .subscribe(object : Consumer<PatternLockCompoundEvent> {
//                @Throws(Exception::class)
//                override fun accept(event: PatternLockCompoundEvent) {
//                    when (event.eventType) {
//                        PatternLockCompoundEvent.EventType.PATTERN_STARTED -> {
//                            Log.d(javaClass.name, "Pattern drawing started")
//                        }
//                        PatternLockCompoundEvent.EventType.PATTERN_PROGRESS -> {
//                            Log.d(
//                                javaClass.name, "Pattern progress: " +
//                                        PatternLockUtils.patternToString(
//                                            binding.loutPinInputPattern,
//                                            event.pattern
//                                        )
//                            )
//                        }
//                        PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> {
//                            Log.d(
//                                javaClass.name, "Pattern complete: " +
//                                        PatternLockUtils.patternToString(
//                                            binding.loutPinInputPattern,
//                                            event.pattern
//                                        )
//                            )
//                        }
//                        PatternLockCompoundEvent.EventType.PATTERN_CLEARED -> {
//                            Log.d(javaClass.name, "Pattern has been cleared")
//                        }
//                    }
//                }
//            })
//
//        binding.loutPinInputPattern.run {
//
//            dotCount = 3
//            dotNormalSize = ResourceUtils.getDimensionInPx(
//                context,
//                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_size
//            ).toInt()
//            dotSelectedSize = ResourceUtils.getDimensionInPx(
//                context,
//                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_selected_size
//            ).toInt()
//            pathWidth = ResourceUtils.getDimensionInPx(
//                context,
//                com.andrognito.patternlockview.R.dimen.pattern_lock_path_width
//            ).toInt()
//            isAspectRatioEnabled = true
//            aspectRatio = PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
//            setViewMode(PatternLockView.PatternViewMode.CORRECT)
//            dotAnimationDuration = 150
//            pathEndAnimationDuration = 100
//            wrongStateColor = ResourceUtils.getColor(
//                context,
//                R.color.red
//            )
//            isInStealthMode = false
//            isTactileFeedbackEnabled = true
//            isInputEnabled = true
//            addPatternLockListener(mPatternLockViewListener)
//        }
//    }
//
//    private var mPatternLockViewListener = object : PatternLockViewListener {
//        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
//
//            if (preferences.password == PatternLockUtils.patternToString(
//                    binding.loutPinInputPattern,
//                    pattern
//                )
//            ) {
//                preferences.lockPin = false
//                preferences.password =
//                    PatternLockUtils.patternToString(binding.loutPinInputPattern, pattern)
//                binding.loutPinInputPattern.clearPattern()
//                clearView()
//
//            } else {
//                if (preferences.password == "") {
//                    preferences.password =
//                        PatternLockUtils.patternToString(binding.loutPinInputPattern, pattern)
//                }
//                nextStep()
//                Handler().postDelayed({
//                    binding.loutPinInputPattern.clearPattern()
//                }, 500)
//
//            }
//
//        }
//
//
//        override fun onCleared() {
//        }
//
//        override fun onStarted() {
//        }
//
//        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
//        }
//
//    }
//
//
//    override
//    fun onTextComplete(enteredText: String): Boolean {
//        if (isFirstInit) {
//            if (currentStep == 1) {
//                if (binding.inputField.text.toString() == finalCode) {
//                    preferences.password = finalCode
//                    preferences.lockPin = true
//                    clearView()
//
//                } else {
//                    binding.title.text = context.getString(R.string.wrong_pin_code)
//                    pinCode = ""
//                    binding.inputField.setText(pinCode)
//                }
//                return true
//            }
//            nextStep()
//        } else {
//            if (enteredText == preferences.password) {
//                clearView()
//
//            }
//        }
//        return true
//    }
//
//    private fun nextStep() {
//        val handler = Handler(Looper.myLooper()!!)
//        val runnable = Runnable {
//            binding.title.text = context.getString(R.string.enter_pin_code_again)
//            finalCode = pinCode
//            pinCode = ""
//            binding.inputField.setText(pinCode)
//            currentStep++
//            binding.step.go(currentStep, true)
//        }
//        handler.postDelayed(runnable, 500)
//    }

}