package com.lgi.applock.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lgi.applock.fragments.ThemeFragment
import com.lgi.applock.models.Theme

class PageThemeAdapter(manager: FragmentManager, lifeCycle: Lifecycle, var listTheme: List<Theme>) : FragmentStateAdapter(manager, lifeCycle){
    override fun getItemCount(): Int {
        return listTheme.size
    }

    override fun createFragment(position: Int): Fragment {
        return ThemeFragment.newInstance()
    }


}

