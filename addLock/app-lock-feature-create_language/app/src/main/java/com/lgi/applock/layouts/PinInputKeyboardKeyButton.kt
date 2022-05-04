package com.lgi.applock.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lgi.applock.R
import com.lgi.applock.databinding.PinInputKeyboardKeyButtonBinding

class PinInputKeyboardKeyButton: ConstraintLayout {
    private var textFromAttrs = ""
    private var binding: PinInputKeyboardKeyButtonBinding? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        initAttribute(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
        initAttribute(context, attrs)
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = PinInputKeyboardKeyButtonBinding.inflate(inflater, this, true)
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.pin_input_button_text))
        textFromAttrs = arr.getString(0).toString()
        setText(textFromAttrs)
        arr.recycle()
    }

    fun setText(text: String) {
        textFromAttrs = text
        binding?.tvNum?.text = text
    }

    fun getText(): String = textFromAttrs

    fun setButtonColor(color: Int) = with(binding!!) {
        loutMainWrapper.backgroundColor = color
        invalidate()
    }

    fun setButtonTextColor(color: Int) = with(binding!!) {
        tvNum.setTextColor(color)
        invalidate()
    }
}