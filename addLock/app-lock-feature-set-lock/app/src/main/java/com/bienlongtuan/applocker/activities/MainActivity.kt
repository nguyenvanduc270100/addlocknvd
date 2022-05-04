package com.bienlongtuan.applocker.activities

import android.content.Intent
import android.os.Bundle
import com.bienlongtuan.applocker.databinding.ActivityMainBinding
import com.bienlongtuan.applocker.fragments.BlankFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.appCompatTextView.text = "Lmao"

        binding.appCompatTextView.setOnClickListener {
            startActivity(Intent(this@MainActivity,SetPinActivity::class.java))
        }

        addFragmentToView(
            BlankFragment(),
            binding.viewFragmentContainer
        )
    }
}