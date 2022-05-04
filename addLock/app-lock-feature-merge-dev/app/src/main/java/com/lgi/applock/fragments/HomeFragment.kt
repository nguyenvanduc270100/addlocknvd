package com.lgi.applock.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lgi.applock.activities.LockAppActivity
import com.lgi.applock.adapters.AppLockAdapter
import com.lgi.applock.databinding.FragmentHomeBinding
import com.lgi.applock.models.AppLockModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)

    }

}