package com.bienlongtuan.applocker.layouts

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.MainActivityVpTabsItemBinding
import com.bumptech.glide.Glide

class MainActivityTabItem: ConstraintLayout {
    companion object {
        @JvmStatic
        fun create(context: Context, title: String, icResourceId: Int, defaultState: Boolean): MainActivityTabItem {
            val layout = MainActivityTabItem(context)
            layout.setTitle(title)
            layout.setIcon(icResourceId)
            layout.setActive(defaultState)
            return layout
        }
    }

    private var binding: MainActivityVpTabsItemBinding? = null

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
        binding = MainActivityVpTabsItemBinding.inflate(inflater, this, true)
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.pin_input_button_text))
        arr.recycle()
    }

    fun setActive(value: Boolean) = with(binding!!) {
        if (value) {
            loutMainWrapper.backgroundColor = ContextCompat.getColor(
                context,
                R.color.home_tab_bar_wrapper_active
            )
            tvTitle.visibility = VISIBLE
            imvTabIcon.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.home_tab_bar_icon_active
                ),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            loutMainWrapper.backgroundColor = ContextCompat.getColor(
                context,
                R.color.transparent
            )
            tvTitle.visibility = GONE
            imvTabIcon.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.home_tab_bar_icon_inactive
                ),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun setTitle(title: String) = with(binding!!) {
        tvTitle.text = title
    }

    fun setIcon(resourceId: Int) = with(binding!!) {
        Glide.with(context)
            .load(resourceId)
            .centerInside()
            .into(imvTabIcon)
    }
}