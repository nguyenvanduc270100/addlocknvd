package com.lgi.applock.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgi.applock.databinding.FragmentToolBinding
import com.lgi.applock.fragments.BaseFragment

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