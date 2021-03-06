/*
* BaseActivity.kt
* Author: Long Nguyen
* Date: 7/4/2022
* */

package com.lgi.applock.activities

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lgi.applock.App
import com.lgi.applock.utils.ContextUtils
import java.util.*

abstract class BaseActivity<V: ViewBinding> : AppCompatActivity() {
    lateinit var binding: V

    abstract fun setViewBinding(): V

    override fun attachBaseContext(newBase: Context) {
        val localeToSwitchTo = Locale(App.language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setViewBinding()
        setContentView(binding.root)

        // make full transparent statusBar
        val visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = visibility
        val windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        setWindowFlag(windowManager, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        window.attributes = winParams
    }

    /*
    * Th??m fragment v??o view
    * - fragment: Fragment mu???n th??m
    * - view: View mu???n s??? d???ng l??m container cho fragment (N??n l?? FrameLayout)
    * - method (optional): 0 - add, 1 - replace. Default: 1
    * - addToBackStack (optional): Th??m fragment v??o backstack. Default: true
    * */
    fun addFragmentToView(
        fragment: Fragment,
        view: View,
        method: Int? = 0,
        addToBackStack: Boolean? = true
    ) {
        try {
            val mFragmentManager = supportFragmentManager
            val mFragmentTransaction = mFragmentManager.beginTransaction()
            when (method) {
                0 -> mFragmentTransaction.add(view.id, fragment)
                else -> mFragmentTransaction.replace(view.id, fragment)
            }
            if (addToBackStack == true) {
                mFragmentTransaction.addToBackStack(null)
            }
            mFragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BaseActivity", e.message!!)
        }
    }
}