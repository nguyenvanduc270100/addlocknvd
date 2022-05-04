package com.bienlongtuan.applocker.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bienlongtuan.applocker.databinding.FragmentBlankBinding

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBlankBinding {
        return FragmentBlankBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.tvTest.text = "Trầm cảm lên"
    }
}