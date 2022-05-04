package com.bienlongtuan.applocker.activities

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivityChooseImageBinding
import com.blankj.utilcode.util.ToastUtils

class ChooseImageActivity : BaseActivity<ActivityChooseImageBinding>() {
    override fun setViewBinding() = ActivityChooseImageBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
        setupToolbar()
    }

    private fun setupToolbar() = with(binding.toolbar) {
        setTitle(R.string.chose_photo)
        setTitleTextColor(Color.WHITE)
        navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
        setNavigationOnClickListener {
            finish()
        }
        inflateMenu(R.menu.menu_choose_image)
        setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.done -> {
                    ToastUtils.showShort("Nai")
                }
            }
            true
        }
    }

    private fun initListener() = with(binding) {
        photoPicker.setOnClickListener {
            photoPicker.setTextColor(
                ContextCompat.getColor(
                    this@ChooseImageActivity,
                    R.color.main_color
                )
            )
            photoPicker.setTypeface(null,Typeface.BOLD)
            albumPicker.setTypeface(null,Typeface.NORMAL)
            albumPicker.setTextColor(Color.BLACK)
        }

        albumPicker.setOnClickListener {
            albumPicker.setTextColor(
                ContextCompat.getColor(
                    this@ChooseImageActivity,
                    R.color.main_color
                )
            )
            albumPicker.setTypeface(null,Typeface.BOLD)
            photoPicker.setTypeface(null,Typeface.NORMAL)
            photoPicker.setTextColor(Color.BLACK)
        }
    }

}