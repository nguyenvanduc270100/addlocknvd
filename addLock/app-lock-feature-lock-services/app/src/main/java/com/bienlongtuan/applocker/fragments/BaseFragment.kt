/*
* BaseFragment.kt
* Author: Long Nguyen
* Date: 7/4/2022
* */

package com.bienlongtuan.applocker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V: ViewBinding>: Fragment() {
    lateinit var binding: V

    abstract fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): V

    abstract fun onCreateOurView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setViewBinding(inflater, container)

        onCreateOurView()

        return binding.root
    }
}