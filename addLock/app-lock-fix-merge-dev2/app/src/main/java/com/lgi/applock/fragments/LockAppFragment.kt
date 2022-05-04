package com.lgi.applock.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgi.applock.databinding.FragmentLockAppBinding

class LockAppFragment : BaseFragment<FragmentLockAppBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLockAppBinding {
        return FragmentLockAppBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {

    }

}