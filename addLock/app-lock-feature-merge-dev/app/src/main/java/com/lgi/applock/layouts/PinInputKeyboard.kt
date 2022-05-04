package com.lgi.applock.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lgi.applock.R
import com.lgi.applock.databinding.PinInputKeyboardLayoutBinding

class PinInputKeyboard : ConstraintLayout {
    private lateinit var binding: PinInputKeyboardLayoutBinding
    var onPressPinInputKeyListener: OnPressPinInputKeyListener? = null

    interface OnPressPinInputKeyListener {
        fun onClickKey(keyString: String)

        fun onClickDelete();
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        initAttribute(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
        initAttribute(context, attrs)
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = PinInputKeyboardLayoutBinding.inflate(inflater, this, true)

        // Set on key press
        binding.btnKey0.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey0.getText()) }
        binding.btnKey1.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey1.getText()) }
        binding.btnKey2.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey2.getText()) }
        binding.btnKey3.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey3.getText()) }
        binding.btnKey4.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey4.getText()) }
        binding.btnKey5.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey5.getText()) }
        binding.btnKey6.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey6.getText()) }
        binding.btnKey7.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey7.getText()) }
        binding.btnKey8.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey8.getText()) }
        binding.btnKey9.setOnClickListener { onPressPinInputKeyListener?.onClickKey(binding.btnKey9.getText()) }
        binding.btnDelete.setOnClickListener { onPressPinInputKeyListener?.onClickDelete() }
    }

    fun changeKeyboardColor(color: Int) = with(binding) {
        btnKey0.setButtonColor(color)
        btnKey1.setButtonColor(color)
        btnKey2.setButtonColor(color)
        btnKey3.setButtonColor(color)
        btnKey4.setButtonColor(color)
        btnKey5.setButtonColor(color)
        btnKey6.setButtonColor(color)
        btnKey7.setButtonColor(color)
        btnKey8.setButtonColor(color)
        btnKey9.setButtonColor(color)
        invalidate()
    }

    fun changeKeyboardCharacterColor(color: Int) = with(binding) {
        btnKey0.setButtonTextColor(color)
        btnKey1.setButtonTextColor(color)
        btnKey2.setButtonTextColor(color)
        btnKey3.setButtonTextColor(color)
        btnKey4.setButtonTextColor(color)
        btnKey5.setButtonTextColor(color)
        btnKey6.setButtonTextColor(color)
        btnKey7.setButtonTextColor(color)
        btnKey8.setButtonTextColor(color)
        btnKey9.setButtonTextColor(color)
        tvBtnDelete.setTextColor(color)
        invalidate()
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.pin_input_button_text))
        arr.recycle()
    }
}