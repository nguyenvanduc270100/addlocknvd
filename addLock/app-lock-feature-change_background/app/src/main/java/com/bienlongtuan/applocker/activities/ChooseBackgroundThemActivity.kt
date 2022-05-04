package com.bienlongtuan.applocker.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.bienlongtuan.applocker.adapters.v2.ImageAdapterV2
import com.bienlongtuan.applocker.databinding.ActivityChooseBackgroundThemeBinding
import com.bienlongtuan.applocker.helpers.ImageDirectory
import com.bienlongtuan.applocker.helpers.ImageInfo
import com.bienlongtuan.applocker.utils.BackgroundUtils
import com.bienlongtuan.applocker.utils.MediaFileBusiness
import com.bienlongtuan.applocker.values.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class ChooseBackgroundThemActivity : BaseActivity<ActivityChooseBackgroundThemeBinding>() {
    override fun setViewBinding() = ActivityChooseBackgroundThemeBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        val backgroundResource = BackgroundUtils.getBackground(this@ChooseBackgroundThemActivity)
        if (backgroundResource != "") {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.centerInside()
                .transform(CenterInside(), GranularRoundedCorners(20f, 20f, 0f, 0f))
            Glide.with(this@ChooseBackgroundThemActivity).load(backgroundResource)
                .apply(requestOptions)
                .into(object : CustomViewTarget<ConstraintLayout, Drawable>(binding.root) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        binding.rltBackground.background = resource
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                    }

                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setupToolbar()
        setupListener()
    }

    private fun setupListener() {

    }

    private fun setupToolbar() = with(binding) {
//        toolbar.inflateMenu(R.menu.menu_choose_image)
        toolbar.setNavigationOnClickListener {
            finish()
        }
//        toolbar.setOnMenuItemClickListener {
//            if (it.itemId == R.id.ic_done) {
//                finish()
//            }
//            true
//        }
    }

    private fun getAllImages() {
        val mediaFileBusiness = MediaFileBusiness(this@ChooseBackgroundThemActivity)
        val images = mediaFileBusiness.allMediaDirectorOfRoot

        setupAdapterData(images)
    }

    private fun setupAdapterData(images: ArrayList<ImageDirectory?>) {
        binding.albumList.layoutManager = GridLayoutManager(
            this@ChooseBackgroundThemActivity,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        val adapter = ConcatAdapter()
        binding.albumList.adapter = adapter

        for (image in images) {
            if (images.indexOf(image) != 0) {
                val innerAdapter =
                    ImageAdapterV2(image, object : ImageAdapterV2.ImageClickListener {
                        override fun onImageClick(imageInfo: ImageInfo) {
                            val intent = Intent(
                                this@ChooseBackgroundThemActivity,
                                PreviewBackgroundActivity::class.java
                            )
                            intent.putExtra(Constants.Intent.previewBackground, imageInfo.imagePath)
                            startActivity(intent)
                        }

                    })
                adapter.addAdapter(innerAdapter)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 1){
//            if(resultCode == RESULT_OK){
//                String background
//            }
//        }
//    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@ChooseBackgroundThemActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this@ChooseBackgroundThemActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permission = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this@ChooseBackgroundThemActivity, permission, 1)
        } else {
            getAllImages()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish()
            } else {
                getAllImages()
            }
        }
    }
}