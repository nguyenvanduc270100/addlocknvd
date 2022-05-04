package com.lgi.applock.layouts

import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.lgi.applock.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.lgi.applock.databinding.LayoutTabItemBinding
/**
 * longtx - 19-04-2022
 */
class TabItemCustom : ConstraintLayout {
    private var binding: LayoutTabItemBinding? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_tab_item, this, true)
    }

    fun setTextValue(value: String?) {
        binding!!.text.text = value
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setFocusing(isFocusing: Boolean) {
        if (isFocusing) {
            binding!!.layout.background =
                context!!.resources.getDrawable(R.drawable.tab_background_selected)
            binding!!.text.setTextAppearance(context, R.style.textSelectedTheme)
        } else {
            binding!!.layout.background =
                context!!.resources.getDrawable(R.drawable.tab_background_unselected)
            binding!!.text.setTextAppearance(context, R.style.textNormalTheme)
        }
    }

    companion object {
        fun create(context: Context, initFocus: Boolean, value: String?): TabItemCustom {
            val ottMainChatTabItem = TabItemCustom(context)
            ottMainChatTabItem.setFocusing(initFocus)
            ottMainChatTabItem.setTextValue(value)
            return ottMainChatTabItem
        }
    }
}