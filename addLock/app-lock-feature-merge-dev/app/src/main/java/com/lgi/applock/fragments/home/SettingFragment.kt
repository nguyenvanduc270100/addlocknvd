package com.lgi.applock.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgi.applock.databinding.FragmentSettingBinding
import com.lgi.applock.fragments.BaseFragment

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