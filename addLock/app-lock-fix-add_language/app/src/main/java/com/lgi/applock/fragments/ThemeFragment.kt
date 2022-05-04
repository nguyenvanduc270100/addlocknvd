package com.lgi.applock.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lgi.applock.R
import com.lgi.applock.adapters.ThemeAdapter
import com.lgi.applock.databinding.FragmentThemeBinding
import com.lgi.applock.models.Theme
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

    override fun onCreateOurView() {
        loadList()
        adapter = ThemeAdapter(listTheme, this)
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

    override fun onClickClick(theme: Theme) {
        showCustomdialog(theme)
    }

    private fun showCustomdialog(theme: Theme) {
        val customDialog = Dialog(requireActivity())
        customDialog.setContentView(R.layout.layout_dialog_show_preview)
        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val btnDowload = customDialog.findViewById<AppCompatButton>(R.id.btn_dowload);
        val tvCannel = customDialog.findViewById<TextView>(R.id.tv_cancel)
        val imgTheme = customDialog.findViewById<ImageView>(R.id.img_theme)
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        Glide.with(requireContext()).load(theme.img).centerCrop().into(imgTheme)
        tvCannel.setOnClickListener {
            customDialog.cancel()
        }
        customDialog.show()
    }


}