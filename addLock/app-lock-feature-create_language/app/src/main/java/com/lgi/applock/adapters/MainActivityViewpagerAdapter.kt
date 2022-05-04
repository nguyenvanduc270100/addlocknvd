package com.lgi.applock.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lgi.applock.fragments.BlankFragment
import com.lgi.applock.fragments.HomeFragment
import com.lgi.applock.fragments.HomeThemeFragment

class MainActivityViewpagerAdapter(
    activity: FragmentActivity
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> HomeFragment()
        1 -> HomeThemeFragment()
        2 -> BlankFragment()
        else -> BlankFragment()
    }
}