/*
* BaseActivity.kt
* Author: Long Nguyen
* Date: 7/4/2022
* */

package com.lgi.applock.activities

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<V: ViewBinding> : AppCompatActivity() {
    lateinit var binding: V

    abstract fun setViewBinding(): V

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
    * Thêm fragment vào view
    * - fragment: Fragment muốn thêm
    * - view: View muốn sử dụng làm container cho fragment (Nên là FrameLayout)
    * - method (optional): 0 - add, 1 - replace. Default: 1
    * - addToBackStack (optional): Thêm fragment vào backstack. Default: true
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