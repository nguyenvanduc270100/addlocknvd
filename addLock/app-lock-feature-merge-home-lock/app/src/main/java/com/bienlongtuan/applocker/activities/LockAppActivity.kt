package com.bienlongtuan.applocker.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivityLockAppBinding
import com.bienlongtuan.applocker.utils.Constants
import com.blankj.utilcode.util.ToastUtils
import com.itsxtt.patternlock.PatternLockView
import java.util.ArrayList

class LockAppActivity : BaseActivity<ActivityLockAppBinding>() {

    private lateinit var passWord: String;
    private lateinit var passWordConfirm: String;
    private var isConfirm: Boolean = false;

    override fun setViewBinding() = ActivityLockAppBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference =  getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        if(sharedPreference.getString(Constants.PASSWORD, "")?.isEmpty() == false) gotoHome()

        binding.patternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onStarted() {
                super.onStarted()
            }

            override fun onProgress(ids: ArrayList<Int>) {
                super.onProgress(ids)
            }

            override fun onComplete(ids: ArrayList<Int>): Boolean {
                var pass = ""
                for (item in ids) {
                    pass += item
                }
                if (pass.length <= 3) return false
                return if (!isConfirm) {
                    passWord = pass
                    isConfirm = true
                    setChangeIconStep()
                    true
                } else {
                    passWordConfirm = pass
                    if (passWord == passWordConfirm) {
                        sharedPreference.edit().putString(Constants.PASSWORD, passWord).apply()
                        gotoHome()
                        true
                    } else {
                        toast("Passwords are not the same")
                        false
                    }
                }
            }

        })
    }

    fun gotoHome(){
        val intent = Intent(this@LockAppActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun setChangeIconStep(){
        binding.tvStep2.setBackgroundResource(R.drawable.border_text_circle_selected)
        binding.tvStep2.setTextColor(resources.getColor(R.color.colorPrimary))
        binding.tvDescribeLockDraw.text = resources.getString(R.string.tab_lock_draw_unlock_confirm)
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this@LockAppActivity, message, Toast.LENGTH_SHORT).show()

}