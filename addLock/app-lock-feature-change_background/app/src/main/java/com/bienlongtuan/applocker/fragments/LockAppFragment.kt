package com.bienlongtuan.applocker.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienlongtuan.applocker.adapters.AppLockAdapter
import com.bienlongtuan.applocker.databinding.FragmentHomeBinding
import com.bienlongtuan.applocker.databinding.FragmentLockAppBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.models.AppLockModel

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