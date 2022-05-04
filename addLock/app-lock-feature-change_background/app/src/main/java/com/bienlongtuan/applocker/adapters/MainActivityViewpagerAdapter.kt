package com.bienlongtuan.applocker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bienlongtuan.applocker.fragments.BlankFragment
import com.bienlongtuan.applocker.fragments.HomeFragment

class MainActivityViewpagerAdapter(
    activity: FragmentActivity
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> HomeFragment()
        1 -> BlankFragment()
        2 -> BlankFragment()
        else -> BlankFragment()
    }
}