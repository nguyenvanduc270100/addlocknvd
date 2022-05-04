package com.lgi.applock.fragments

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.lgi.applock.R
import com.lgi.applock.activities.ChangeColorActivity
import com.lgi.applock.activities.ChooseBackgroundThemActivity
import com.lgi.applock.adapters.PageThemeAdapter
import com.lgi.applock.databinding.FragmentHomeThemeBinding
import com.lgi.applock.layouts.TabItemCustom
import com.lgi.applock.models.Theme
import com.lgi.applock.utils.RealPathUtils
import com.lgi.applock.utils.ThemeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * longtx - 18-04-2022
 */

class HomeThemeFragment : BaseFragment<FragmentHomeThemeBinding>(),
    TabLayout.OnTabSelectedListener {
    private lateinit var rvAdapter: PageThemeAdapter
    private lateinit var listTheme: List<Theme>

    private val openGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result?.data
        val path: String =
            intent?.data?.let { RealPathUtils.getPathFromUri(requireContext(), it).toString() }
                .toString()
        ThemeUtils.saveBackground(path, requireActivity())

    }

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeThemeBinding {
        return FragmentHomeThemeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        loadList()
        rvAdapter =
            activity?.let { PageThemeAdapter(it.supportFragmentManager, it.lifecycle, listTheme) }!!
        binding.viewpager.adapter = rvAdapter
        TabLayoutMediator(
            binding.tablayout, binding.viewpager
        ) { tab, position ->
            tab.text = listTheme[position].name
        }.attach()
        binding.tablayout.addOnTabSelectedListener(this)
        for (i in listTheme.indices) {
            binding.tablayout.getTabAt(i + 1)?.customView = TabItemCustom.create(
                requireActivity(),
                false,
                listTheme[i + 1].name
            )
        }
        binding.tablayout.getTabAt(0)?.customView = TabItemCustom.create(
            requireActivity(),
            true,
            listTheme[0].name
        )
        binding.btnAdd.setOnClickListener {
            showDialog()
        }

    }

    private fun loadList() {
        listTheme = listOf(
            Theme("All"),
            Theme("Romantic"),
            Theme("Art"),
            Theme("Nature"),
            Theme("Fashion"),
            Theme("Cute"),
            Theme("Cute"),
            Theme("Cute"),
        )
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_choose_options, null)
        dialog.setContentView(view)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val btnCreateBg = dialog.findViewById<AppCompatButton>(R.id.btn_create)
        val btnChange = dialog.findViewById<AppCompatButton>(R.id.btn_change)
        btnCreateBg?.setOnClickListener {
            showDialogCreateBackground()
        }
        btnChange?.setOnClickListener {
            val intent = Intent(requireContext(), ChangeColorActivity::class.java)
            activity?.startActivity(intent)
        }
        dialog.show()
    }

    //bottom sheet create background
    private fun showDialogCreateBackground() {
        val view = layoutInflater.inflate(R.layout.layout_bottomshet_choose_file, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        dialog.setContentView(view)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel)
        val btnGallery = dialog.findViewById<LinearLayout>(R.id.layout_galeerry)
        val btnFies = dialog.findViewById<LinearLayout>(R.id.layout_files)
        btnCancel?.setOnClickListener {
            dialog.cancel()
        }

        btnGallery?.setOnClickListener {
            val intent = Intent(requireContext(), ChooseBackgroundThemActivity::class.java)
            activity?.startActivity(intent)
        }

        btnFies?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            openGallery.launch(intent)
        }

        dialog.show()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab?.customView is TabItemCustom) (tab.customView as TabItemCustom).setFocusing(
            true
        )
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        if (tab?.customView is TabItemCustom) (tab.customView as TabItemCustom).setFocusing(
            false
        )
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        if (tab?.customView is TabItemCustom) (tab.customView as TabItemCustom).setFocusing(
            true
        )
    }

}