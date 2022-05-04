package com.bienlongtuan.applocker.layouts

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.utils.LayoutUtils

class SafeConstraintLayout: ConstraintLayout {
    var paddingStatusBar = true

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.padding_status_bar))
        paddingStatusBar = arr.getBoolean(0, true)
        arr.recycle()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttribute(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttribute(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (paddingStatusBar && context != null) {
            setPadding(0, LayoutUtils.getStatusBarHeight(context), 0, 0)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}