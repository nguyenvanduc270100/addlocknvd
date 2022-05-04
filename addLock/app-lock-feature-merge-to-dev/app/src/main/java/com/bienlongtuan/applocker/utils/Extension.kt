package com.bienlongtuan.applocker.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Context.showKeyBoard(view: View) {
    if (view.requestFocus()) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        // here is one more tricky issue
        // imm.showSoftInputMethod doesn't work well
        // and imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0) doesn't work well for all cases too
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun View.sunTotal(): Int {
    return this.measuredWidth
}