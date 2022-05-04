package com.lgi.applock.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.lgi.applock.R
import com.lgi.applock.databinding.ActivitySetPinBinding
import com.lgi.applock.layouts.PinInputKeyboard
import com.lgi.applock.utils.PinEncoderUtils
import com.poovam.pinedittextfield.PinField
import com.shuhart.stepview.StepView

class SetPinActivity : BaseActivity<ActivitySetPinBinding>(), PinField.OnTextCompleteListener {
    override fun setViewBinding() = ActivitySetPinBinding.inflate(layoutInflater)
    var pinCode = ""
    private var finalCode = ""
    private var currentStep = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar()
        binding.inputField.showSoftInputOnFocus = false
        binding.loutPinInputKeyboard.onPressPinInputKeyListener = object :
            PinInputKeyboard.OnPressPinInputKeyListener {
            override fun onClickKey(keyString: String) {
                pinCode += keyString
                binding.inputField.setText(pinCode)
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

        binding.step.state.animationType(StepView.ANIMATION_LINE).stepsNumber(2)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
            .commit()


        binding.inputField.onTextCompleteListener = this@SetPinActivity
    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            inflateMenu(R.menu.menu_main)
        }
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
        handler.postDelayed(runnable, 700)
    }

    override fun onTextComplete(enteredText: String): Boolean {
        if (currentStep == 1) {
            if (binding.inputField.text.toString() == finalCode) {
                PinEncoderUtils.savePinCode(finalCode, this@SetPinActivity)
                finish()
            } else {
                binding.title.text = getString(R.string.wrong_pin_code)
                pinCode = ""
                binding.inputField.setText(pinCode)
            }
            return true
        }
        nextStep()
        return true
    }
}