package com.bienlongtuan.applocker.activities

import android.os.Bundle
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivitySetPinBinding

class SetPinActivity : BaseActivity<ActivitySetPinBinding>() {
    override fun setViewBinding() = ActivitySetPinBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar()

    }

    private fun setupToolbar() = with(binding.toolbar){
        title = getString(R.string.app_name)

    }
}