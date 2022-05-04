package com.bienlongtuan.applocker.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bienlongtuan.applocker.databinding.FragmentBlankBinding
import com.bienlongtuan.applocker.databinding.FragmentSettingBinding
import com.bienlongtuan.applocker.fragments.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
    }
}