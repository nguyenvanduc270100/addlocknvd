package com.lgi.applock.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.lgi.applock.R
import com.lgi.applock.databinding.ActivitySetPinBinding
import com.lgi.applock.databinding.ActivitySettingBinding
import com.lgi.applock.fragments.LanguageFragment
import com.lgi.applock.fragments.SettingFragment
import com.lgi.applock.layouts.PinInputKeyboard
import com.lgi.applock.utils.PinEncoderUtils
import com.lgi.applock.utils.preferences.LockPreferences
import com.poovam.pinedittextfield.PinField
import com.shuhart.stepview.StepView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun setViewBinding() = ActivitySettingBinding.inflate(layoutInflater)

    @Inject
    lateinit var preferences: LockPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragmentToView(
            LanguageFragment(preferences),
            binding.layoutContent,
            0,
            false
        )
    }
}