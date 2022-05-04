package com.bienlongtuan.applocker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bienlongtuan.applocker.databinding.ActivityMainBinding
import com.bienlongtuan.applocker.fragments.BlankFragment

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {
    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.appCompatTextView.text = "Lmao"
        binding.appCompatTextView.setOnClickListener(this)

        addFragmentToView(
            BlankFragment(),
            binding.viewFragmentContainer
        )
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this,ChooseImageActivity::class.java))
    }
}