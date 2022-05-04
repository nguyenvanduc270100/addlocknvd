package com.bienlongtuan.applocker.activities

import android.os.Bundle
import com.bienlongtuan.applocker.databinding.ActivityMainBinding
import com.bienlongtuan.applocker.layouts.PinInputKeyboard
import com.blankj.utilcode.util.ToastUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loutPinInputKeyboard.onPressPinInputKeyListener = object :
            PinInputKeyboard.OnPressPinInputKeyListener {
            override fun onClickKey(keyString: String) {
                ToastUtils.showShort(keyString)
            }

            override fun onClickDelete() {
                ToastUtils.showShort("Click delete")
            }
        }
    }
}