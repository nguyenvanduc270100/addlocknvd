package com.lgi.applock.fragments

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgi.applock.App
import com.lgi.applock.R
import com.lgi.applock.activities.MainActivity
import com.lgi.applock.activities.SettingActivity
import com.lgi.applock.databinding.FragmentLanguageBinding
import com.lgi.applock.models.LanguageModel
import com.lgi.applock.adapters.LanguageViewAdapter
import com.lgi.applock.extensions.launchActivity
import com.lgi.applock.utils.ContextUtils
import com.lgi.applock.utils.preferences.LockPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LanguageFragment constructor(private val preferences: LockPreferences) :
    BaseFragment<FragmentLanguageBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLanguageBinding {
        return FragmentLanguageBinding.inflate(layoutInflater, container, false)
    }

    var activity: SettingActivity? = null

    private val mDataAdapter = ArrayList<LanguageModel>()

    override fun onCreateOurView() {

        activity = context as SettingActivity?
        setOnClick()

        val recycleView = binding.rvItems

        mDataAdapter.add(LanguageModel(R.drawable.ic_english, "English", "en"))
        mDataAdapter.add(LanguageModel(R.drawable.ic_indian, "Indian", "hi"))
        mDataAdapter.add(LanguageModel(R.drawable.ic_korean, "Korean", "ko"))
        mDataAdapter.add(LanguageModel(R.drawable.ic_korean, "VietNam", "vn"))
        mDataAdapter.add(LanguageModel(R.drawable.ic_japanese, "Japanese", "ja"))

        val adapter = LanguageViewAdapter(
            mDataAdapter,
            preferences.language,
            object : LanguageViewAdapter.OnItemClick {
                override fun onClick(item: LanguageModel) {
                    if (preferences.language != item.code) {
                        preferences.language = item.code
                        App.language = item.code
                        ContextUtils.updateLocale(requireActivity(), Locale(item.code))
                        requireContext().launchActivity<MainActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    }
                }

            })

        recycleView.adapter = adapter
    }

    private fun setOnClick() {
        binding.toolBar.btnBack.setOnClickListener {
            activity?.onBackPressed()
//            Log.e("aasdasd", "${preferences.password}")
        }
    }

    private fun onRadioChanged() {
    }
}