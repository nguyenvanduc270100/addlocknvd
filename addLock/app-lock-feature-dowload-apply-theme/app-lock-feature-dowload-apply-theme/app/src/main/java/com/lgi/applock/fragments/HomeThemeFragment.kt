package com.lgi.applock.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lgi.applock.R
import com.lgi.applock.activities.ChangeColorActivity
import com.lgi.applock.activities.ChooseBackgroundThemActivity
import com.lgi.applock.activities.PreviewBackgroundActivity
import com.lgi.applock.adapters.PageThemeAdapter
import com.lgi.applock.databinding.FragmentHomeThemeBinding
import com.lgi.applock.databinding.LayoutDialogShowPreviewBinding
import com.lgi.applock.layouts.TabItemCustom
import com.lgi.applock.models.Image
import com.lgi.applock.models.Theme
import com.lgi.applock.utils.AppDownloader
import com.lgi.applock.utils.RealPathUtils
import com.lgi.applock.utils.ThemeUtils
import com.lgi.applock.values.Constants
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.util.concurrent.Executors

/**
 * longtx - 18-04-2022
 */

class HomeThemeFragment : BaseFragment<FragmentHomeThemeBinding>(),
    TabLayout.OnTabSelectedListener {
    private lateinit var rvAdapter: PageThemeAdapter
    private lateinit var listTheme: List<Theme>
    private lateinit var downloadFetchListener: FetchListener
    private lateinit var bindingDialogPre: LayoutDialogShowPreviewBinding

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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onClickPreviewTheme(theme: Image) {
        showCustomdialog(theme)
    }

    override fun onCreateOurView() {
        loadList()
        initListener()
        AppDownloader.getInstance().addListener(downloadFetchListener)
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

    override fun onDestroy() {
        super.onDestroy()
        AppDownloader.getInstance().removeListener(downloadFetchListener)
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

    private fun showCustomdialog(theme: Image) {
        val customDialog = Dialog(requireActivity())
        bindingDialogPre = LayoutDialogShowPreviewBinding.inflate(layoutInflater, null, false)
        customDialog.setContentView(bindingDialogPre.root)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //check neu co file roi thi hien apply
        val file = File(requireContext().filesDir.absolutePath + "/themes/${theme.id}.jpg")
        if (file.exists()) {
            bindingDialogPre.btnDowload.setText("Apply")
            handleClickApply(file.path)
        } else {
            bindingDialogPre.btnDowload.setOnClickListener {
                bindingDialogPre.btnDowload.visibility = View.GONE
                bindingDialogPre.progess.visibility = View.VISIBLE
                handleDowload(theme)
            }
        }
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        Glide.with(requireContext()).load(theme.link).centerCrop().into(bindingDialogPre.imgTheme)
        bindingDialogPre.tvCancel.setOnClickListener {
            customDialog.cancel()
        }
        customDialog.show()
    }

    private fun initListener() {
        downloadFetchListener = object : FetchListener { // Listener cho quá trình download
            override fun onAdded(download: Download) {
            }

            override fun onCancelled(download: Download) {
            }

            override fun onCompleted(download: Download) {
                Handler(Looper.getMainLooper()).postDelayed({
                    bindingDialogPre.btnDowload.visibility = View.VISIBLE
                    bindingDialogPre.progess.visibility = View.GONE
                    bindingDialogPre.btnDowload.setText("Apply")
                    handleClickApply(download.file)
                }, 300)


            }

            override fun onDeleted(download: Download) {
            }

            override fun onDownloadBlockUpdated(
                download: Download,
                downloadBlock: DownloadBlock,
                totalBlocks: Int
            ) {
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
            }

            override fun onPaused(download: Download) {
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
                bindingDialogPre.progess.progress = download.progress
                Log.d(
                    "lmao_downloading_file",
                    "${download.id} - Progress: ${download.progress} - Time Left: $etaInMilliSeconds - Speed: $downloadedBytesPerSecond"
                );
            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            }

            override fun onRemoved(download: Download) {
            }

            override fun onResumed(download: Download) {
            }

            override fun onStarted(
                download: Download,
                downloadBlocks: List<DownloadBlock>,
                totalBlocks: Int
            ) {
            }

            override fun onWaitingNetwork(download: Download) {
            }
        }
    }

    private fun handleDowload(theme: Image) {
        val file = File(requireContext().filesDir.absolutePath + "/themes/${theme.id}.jpg")
        if (file.exists())
            FileUtils.delete(file) // File mà đã tồn tại là sẽ không tải xuống lại nữa nên phải xoá đi
        AppDownloader.getInstance().enqueueDownloadFile(
            theme.link,
            file.absolutePath
        )

    }

    private fun handleClickApply(file : String) {
        bindingDialogPre.btnDowload.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                PreviewBackgroundActivity::class.java
            )
            intent.putExtra(Constants.Intent.previewBackground, file)
            startActivity(intent)
        }
    }

}