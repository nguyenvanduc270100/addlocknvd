package com.lgi.applock.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.FileUtils
import com.lgi.applock.R
import com.lgi.applock.adapters.ThemeAdapter
import com.lgi.applock.databinding.FragmentThemeBinding
import com.lgi.applock.models.Theme
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lgi.applock.activities.PreviewBackgroundActivity
import com.lgi.applock.helpers.Api
import com.lgi.applock.helpers.RetrofitHelper
import com.lgi.applock.models.Image
import com.lgi.applock.utils.AppDownloader
import com.lgi.applock.values.Constants
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * longtx - 19-04-2022
 */
class ThemeFragment : BaseFragment<FragmentThemeBinding>(), ThemeAdapter.OnItemClickListener {
    private lateinit var adapter: ThemeAdapter
    private lateinit var listTheme: List<Theme>

    companion object {
        fun newInstance(): ThemeFragment {
            return ThemeFragment()
        }
    }


    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThemeBinding {
        return FragmentThemeBinding.inflate(layoutInflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        val retrofitBuilder = RetrofitHelper.getInstance().create(Api::class.java).getListTheme()
        retrofitBuilder.enqueue(object : Callback<List<Image>?> {
            override fun onResponse(call: Call<List<Image>?>, response: Response<List<Image>?>) {
                response.body()?.let { adapter.setList(it) }
            }

            override fun onFailure(call: Call<List<Image>?>, t: Throwable) {
                Log.d("ptit", "onResponse: " + t.localizedMessage)
            }
        })
    }


    override fun onCreateOurView() {
        loadList()
        adapter = ThemeAdapter(this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvc.adapter = adapter
        binding.rvc.layoutManager = layoutManager

    }


    private fun loadList() {
        listTheme = listOf(
            Theme("1", R.drawable.image_theme),
            Theme("2", R.drawable.image_theme_1),
            Theme("3", R.drawable.image_theme),
            Theme("4", R.drawable.image_theme_1),
            Theme("5", R.drawable.image_theme),

            )
    }

    override fun onClickClick(theme: Image) {
        //showCustomdialog(theme)
        EventBus.getDefault().post(theme)
    }


}