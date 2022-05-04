package com.lgi.applock.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.lgi.applock.adapters.v2.ImageAdapterV2
import com.lgi.applock.databinding.ActivityChooseBackgroundThemeBinding
import com.lgi.applock.helpers.ImageDirectory
import com.lgi.applock.helpers.ImageInfo
import com.lgi.applock.utils.MediaFileBusiness
import com.lgi.applock.values.Constants

class ChooseBackgroundThemActivity : BaseActivity<ActivityChooseBackgroundThemeBinding>() {
    override fun setViewBinding() = ActivityChooseBackgroundThemeBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setupToolbar()
        setupListener()
    }

    private fun setupListener() {

    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
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