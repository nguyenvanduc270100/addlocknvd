package com.lgi.applock.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lgi.applock.databinding.ActivityLockAppBinding

class LockAppActivity : BaseActivity<ActivityLockAppBinding>() {

    private lateinit var passWord: String;
    private lateinit var passWordConfirm: String;
    private var isConfirm: Boolean = false;

    override fun setViewBinding() = ActivityLockAppBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        val sharedPreference =  getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
//        if(sharedPreference.getString(Constants.PASSWORD, "")?.isEmpty() == false) gotoHome()
//
//        binding.patternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {
//            override fun onStarted() {
//                super.onStarted()
//            }

    }
//
//    fun gotoHome(){
//        val intent = Intent(this@LockAppActivity, MainActivity2::class.java)
//        startActivity(intent)
//    }
//
//    fun setChangeIconStep(){
//        binding.tvStep2.setBackgroundResource(R.drawable.border_text_circle_selected)
//        binding.tvStep2.setTextColor(resources.getColor(R.color.colorPrimary))
//        binding.tvDescribeLockDraw.text = resources.getString(R.string.tab_lock_draw_unlock_confirm)
//    }
//
//    fun Context.toast(message: CharSequence) =
//        Toast.makeText(this@LockAppActivity, message, Toast.LENGTH_SHORT).show()

}