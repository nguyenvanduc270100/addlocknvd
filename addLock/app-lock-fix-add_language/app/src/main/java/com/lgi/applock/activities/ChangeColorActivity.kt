package com.lgi.applock.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.lgi.applock.R
import com.lgi.applock.databinding.ActivityChangeColorBinding
import com.lgi.applock.utils.ThemeUtils
import com.tistory.zladnrms.roundablelayout.RoundableLayout

/**
 * trinh xuan long - 19/04/20222
 */

class ChangeColorActivity : BaseActivity<ActivityChangeColorBinding>() {
    override fun setViewBinding() = ActivityChangeColorBinding.inflate(layoutInflater)
    private var prePosition: Int = 7;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleButton1(binding.layoutParent1, 1)
        handleButton1(binding.layoutParent2, 2)
        handleButton1(binding.layoutParent3, 3)
        handleButton1(binding.layoutParent4, 4)
        handleButton1(binding.layoutParent5, 5)
        handleButton1(binding.layoutParent6, 6)
        handleButton1(binding.layoutParent7, 7)
        handleButton1(binding.layoutParent8, 8)
        handleInit()
        binding.imgBack.setOnClickListener {
            finish()
        }
        handleSave()
    }

    private fun handleInit() {
        val p = ThemeUtils.getTheme(this)
        prePosition = p
        handleInitValue(prePosition)
        binding.inputField.fieldBgColor = getValue(prePosition)
        binding.loutPinInputKeyboard.changeKeyboardColor(Color.parseColor(ThemeUtils.getColor(this)))
        binding.loutPinInputKeyboard.changeKeyboardCharacterColor(
            Color.parseColor(
                ThemeUtils.getColorKeyboard(
                    this
                )
            )
        )
    }

    private fun handleChangeBgKeyboard(p: Int) {
        if (p == 1) {
            binding.loutPinInputKeyboard.changeKeyboardColor(getValue(1))
            binding.loutPinInputKeyboard.changeKeyboardCharacterColor(getValue(3))
        } else {
            binding.loutPinInputKeyboard.changeKeyboardColor(getValue(p))
            binding.loutPinInputKeyboard.changeKeyboardCharacterColor(getValue(1))
        }
    }

    private fun handleInitValue(position: Int) {
        when (position) {
            1 -> handleChecked(binding.layoutParent1)
            2 -> handleChecked(binding.layoutParent2)
            3 -> handleChecked(binding.layoutParent3)
            4 -> handleChecked(binding.layoutParent4)
            5 -> handleChecked(binding.layoutParent5)
            6 -> handleChecked(binding.layoutParent6)
            7 -> handleChecked(binding.layoutParent7)
            8 -> handleChecked(binding.layoutParent8)
        }
    }

    private fun handleSave() {
        binding.btnSave.setOnClickListener {
            ThemeUtils.saveTheme(
                prePosition,
                getValueString(prePosition),
                getStringKeyboard(prePosition),
                this
            )
            finish()
        }
    }

    private fun handleButton1(layout: RoundableLayout, position: Int) {
        layout.setOnClickListener {
            handleUnchecked()
            handleChangeBgKeyboard(position)
            prePosition = position;
            binding.inputField.fieldBgColor = getValue(prePosition)
            layout.backgroundColor = getValue(position)
            handleChecked(layout)
        }
    }

    private fun getStringKeyboard(p: Int): String {
        var intColor: Int = 0
        if (p == 1) {
            intColor = resources.getColor(R.color.color_list_3) // color for background black
        } else {
            intColor = resources.getColor(R.color.color_list_1) // color text another
        }
        return String.format("#%06X", 0xFFFFFF and intColor)
    }

    private fun getValue(position: Int): Int {
        when (position) {
            1 -> return ContextCompat.getColor(this, R.color.color_list_1)
            2 -> return ContextCompat.getColor(this, R.color.color_list_2)
            3 -> return ContextCompat.getColor(this, R.color.color_list_3)
            4 -> return ContextCompat.getColor(this, R.color.color_list_4)
            5 -> return ContextCompat.getColor(this, R.color.color_list_5)
            6 -> return ContextCompat.getColor(this, R.color.color_list_6)
            7 -> return ContextCompat.getColor(this, R.color.color_list_7)
            8 -> return ContextCompat.getColor(this, R.color.color_list_8)
        }
        return ContextCompat.getColor(this, R.color.color_list_8)
    }

    private fun getValueString(position: Int): String {
        var intColor: Int = 0
        when (position) {
            1 -> intColor = resources.getColor(R.color.color_list_1)
            2 -> intColor = resources.getColor(R.color.color_list_2)
            3 -> intColor = resources.getColor(R.color.color_list_3)
            4 -> intColor = resources.getColor(R.color.color_list_4)
            5 -> intColor = resources.getColor(R.color.color_list_5)
            6 -> intColor = resources.getColor(R.color.color_list_6)
            7 -> intColor = resources.getColor(R.color.color_list_7)
            8 -> intColor = resources.getColor(R.color.color_list_8)

        }
        return String.format("#%06X", 0xFFFFFF and intColor)
    }


    private fun handleChecked(layout: RoundableLayout) {
        layout.strokeLineColor = ContextCompat.getColor(this, R.color.color_checked)
        layout.backgroundColor = ContextCompat.getColor(this, R.color.white)
        layout.strokeLineWidth = 2F
    }

    private fun handleUnchecked() {
        when (prePosition) {
            1 -> {
                Log.d("ptit", "handleUnchecked: ")
                binding.layoutParent1.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_checked)
                binding.layoutParent1.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_1)
            }
            2 -> {
                binding.layoutParent2.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_2)
                binding.layoutParent2.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_2)
            }
            3 -> {
                binding.layoutParent3.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_3)
                binding.layoutParent3.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_3)
            }
            4 -> {
                binding.layoutParent4.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_4)
                binding.layoutParent4.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_4)
            }
            5 -> {
                binding.layoutParent5.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_5)
                binding.layoutParent5.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_5)
            }
            6 -> {
                binding.layoutParent6.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_6)
                binding.layoutParent6.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_6)
            }
            7 -> {
                binding.layoutParent7.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_7)
                binding.layoutParent7.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_7)
            }
            8 -> {
                binding.layoutParent8.strokeLineColor =
                    ContextCompat.getColor(this, R.color.color_list_8)
                binding.layoutParent8.backgroundColor =
                    ContextCompat.getColor(this, R.color.color_list_8)
            }
        }
    }


}