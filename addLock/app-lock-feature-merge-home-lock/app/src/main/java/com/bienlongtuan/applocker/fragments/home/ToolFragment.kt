package com.bienlongtuan.applocker.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bienlongtuan.applocker.databinding.FragmentBlankBinding
import com.bienlongtuan.applocker.databinding.FragmentToolBinding
import com.bienlongtuan.applocker.fragments.BaseFragment

class ToolFragment : BaseFragment<FragmentToolBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToolBinding {
        return FragmentToolBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
    }
}