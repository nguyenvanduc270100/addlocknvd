package com.bienlongtuan.applocker.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivityPreviewBackgroundBinding
import com.bienlongtuan.applocker.helpers.ImageInfo
import com.bienlongtuan.applocker.utils.BackgroundUtils
import com.bienlongtuan.applocker.values.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class PreviewBackgroundActivity : BaseActivity<ActivityPreviewBackgroundBinding>() {
    override fun setViewBinding() = ActivityPreviewBackgroundBinding.inflate(layoutInflater)

    private var currentImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        getDataAndInit()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.inflateMenu(R.menu.menu_choose_image)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.ic_done) {
                currentImage?.let { it1 -> BackgroundUtils.saveBackground(it1,this@PreviewBackgroundActivity) }
                finish()
            }
            true
        }
    }

    private fun getDataAndInit() {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.centerInside().transform(CenterInside(), GranularRoundedCorners(20f,20f,0f,0f))
        currentImage = intent.extras?.getString(Constants.Intent.previewBackground)
        Glide.with(this@PreviewBackgroundActivity).load(currentImage).apply(requestOptions)
            .into(object : CustomViewTarget<ConstraintLayout, Drawable>(binding.root) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.backgroundPreview.background = resource
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

            })
    }


}