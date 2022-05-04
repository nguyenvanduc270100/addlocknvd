package com.lgi.applock.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import com.lgi.applock.databinding.ActivitySetPinBinding
import com.lgi.applock.layouts.PinInputKeyboard
import com.lgi.applock.models.LockState
import com.lgi.applock.utils.Constants
import com.lgi.applock.utils.ThemeUtils
import com.lgi.applock.utils.preferences.LockPreferences
import com.poovam.pinedittextfield.PinField
import com.shuhart.stepview.StepView
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


class SetPinActivity : BaseActivity<ActivitySetPinBinding>(), PinField.OnTextCompleteListener {
    override fun setViewBinding() = ActivitySetPinBinding.inflate(layoutInflater)
    var pinCode = ""
    private var finalCode = ""
    private var currentStep = 0
    private var isFirstInit = false
    private var isServices = false

    @Inject
    lateinit var preferences: LockPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = LockPreferences()
        val intent = intent

        initTheme()

        if (intent.extras?.getBoolean(Constants.SET_PIN_FIRST_TIME) == true) {
            isFirstInit = true
            setupToolbar()
            setupFirstTimeInitLock()
        } else {
            initLockScreen()
        }
        setupFunction()

    }

    private fun startMainOrFinish() {

        val broadcast = Intent("LOCKEDAPP")

        if (isServices) {
            applicationContext.sendBroadcast(broadcast)
            finish()
            return
        }

        val intent = Intent(this@SetPinActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initLockScreen() {
        if (preferences.lockPin == true) {
            showPinLock()
        } else {
            showPatternLock()
        }
    }

    private fun initTheme() {
        binding.loutPinInputKeyboard.changeKeyboardColor(Color.parseColor(ThemeUtils.getColor(this)))
        binding.loutPinInputKeyboard.changeKeyboardCharacterColor(
            Color.parseColor(
                ThemeUtils.getColorKeyboard(
                    this
                )
            )
        )
        binding.inputField.fieldBgColor = Color.parseColor(ThemeUtils.getColor(this))
        binding.loutPinInputPattern.normalStateColor = Color.parseColor(ThemeUtils.getColor(this))

        var requestOption = RequestOptions()
        requestOption = requestOption.centerInside()
            .transform(CenterInside(), GranularRoundedCorners(30f, 30f, 0f, 0f))
        Glide.with(this).load(ThemeUtils.getBackground(this)).centerCrop().apply(requestOption)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    binding.background.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.background.background = placeholder
                }

            })
    }

    private fun setupFunction(){
        binding.inputField.showSoftInputOnFocus = false
        binding.loutPinInputKeyboard.onPressPinInputKeyListener = object :
            PinInputKeyboard.OnPressPinInputKeyListener {
            override fun onClickKey(keyString: String) {
                if (binding.inputField.text.toString().length < 4) {
                    pinCode += keyString
                    binding.inputField.setText(pinCode)
                }
            }

            override fun onClickDelete() {
                if (pinCode == "") {
                    return
                } else {
                    pinCode = pinCode.substring(0, pinCode.length - 1)
                }
                binding.inputField.setText(pinCode)
            }
        }
        binding.inputField.onTextCompleteListener = this@SetPinActivity
        binding.step.state.animationType(StepView.ANIMATION_LINE).stepsNumber(2)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()

        setupPattern()
    }


    private fun setupFirstTimeInitLock() {
        binding.title.visibility = View.VISIBLE
        binding.step.visibility = View.VISIBLE

        preferences.lockPin = true

    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.lock) {
                    switchPinLockType()
                }
                true
            }
        }
    }

    private fun switchPinLockType() {
        if (preferences.lockPin == false) {
            preferences.lockPin = true
            showPinLock()
        } else {
            preferences.lockPin = false
            showPatternLock()
        }
    }

    private fun showPinLock() {
        binding.loutPinInputPattern.visibility = View.GONE
        binding.loutPinInputKeyboard.visibility = View.VISIBLE
        binding.inputField.visibility = View.VISIBLE
    }

    private fun showPatternLock() {
        binding.loutPinInputPattern.visibility = View.VISIBLE
        binding.loutPinInputKeyboard.visibility = View.GONE
        binding.inputField.visibility = View.GONE
    }

    private fun nextStep() {
        val handler = Handler(Looper.myLooper()!!)
        val runnable = Runnable {
            binding.title.text = getString(R.string.enter_pin_code_again)
            finalCode = pinCode
            pinCode = ""
            binding.inputField.setText(pinCode)
            currentStep++
            binding.step.go(currentStep, true)
        }
        handler.postDelayed(runnable, 500)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    override fun onTextComplete(enteredText: String): Boolean {
        if (isFirstInit) {
            if (currentStep == 1) {
                if (binding.inputField.text.toString() == finalCode) {
                    preferences.password = finalCode
                    preferences.lockPin = true
                    EventBus.getDefault().post(LockState(true))
                    startMainOrFinish()
                } else {
                    binding.title.text = getString(R.string.wrong_pin_code)
                    pinCode = ""
                    binding.inputField.setText(pinCode)
                }
                return true
            }
            nextStep()
        } else {
            if (enteredText == preferences.password) {
                EventBus.getDefault().post(LockState(true))
                startMainOrFinish()
            }
        }
        return true
    }


    private var mPatternLockViewListener = object : PatternLockViewListener {
        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {

            if (preferences.password == PatternLockUtils.patternToString(
                    binding.loutPinInputPattern,
                    pattern
                )
            ) {
                preferences.lockPin = false
                preferences.password =
                    PatternLockUtils.patternToString(binding.loutPinInputPattern, pattern)
                binding.loutPinInputPattern.clearPattern()
                EventBus.getDefault().post(LockState(true))
                startMainOrFinish()

            } else {
                if (preferences.password == "") {
                    preferences.password =
                        PatternLockUtils.patternToString(binding.loutPinInputPattern, pattern)
                }
                nextStep()
                Handler().postDelayed({
                    binding.loutPinInputPattern.clearPattern()
                }, 500)

            }

        }


        override fun onCleared() {
        }

        override fun onStarted() {
        }

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
        }

    }

    @SuppressLint("CheckResult")
    private fun setupPattern() {

        RxPatternLockView.patternComplete(binding.loutPinInputPattern)
            .subscribe(object : Consumer<PatternLockCompleteEvent> {
                @Throws(Exception::class)
                override fun accept(patternLockCompleteEvent: PatternLockCompleteEvent) {
                    Log.d(
                        javaClass.name,
                        "Complete: " + patternLockCompleteEvent.pattern.toString()
                    )
                }
            })

        RxPatternLockView.patternChanges(binding.loutPinInputPattern)
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
                                            binding.loutPinInputPattern,
                                            event.pattern
                                        )
                            )
                        }
                        PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> {
                            Log.d(
                                javaClass.name, "Pattern complete: " +
                                        PatternLockUtils.patternToString(
                                            binding.loutPinInputPattern,
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

        binding.loutPinInputPattern.run {

            dotCount = 3
            dotNormalSize = ResourceUtils.getDimensionInPx(
                this@SetPinActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_size
            ).toInt()
            dotSelectedSize = ResourceUtils.getDimensionInPx(
                this@SetPinActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_dot_selected_size
            ).toInt()
            pathWidth = ResourceUtils.getDimensionInPx(
                this@SetPinActivity,
                com.andrognito.patternlockview.R.dimen.pattern_lock_path_width
            ).toInt()
            isAspectRatioEnabled = true
            aspectRatio = PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
            setViewMode(PatternLockView.PatternViewMode.CORRECT)
            dotAnimationDuration = 150
            pathEndAnimationDuration = 100
            wrongStateColor = ResourceUtils.getColor(
                this@SetPinActivity,
                R.color.red
            )
            isInStealthMode = false
            isTactileFeedbackEnabled = true
            isInputEnabled = true
            addPatternLockListener(mPatternLockViewListener)
        }
    }

}