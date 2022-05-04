package com.lgi.applock.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val titles: ArrayList<String> = ArrayList()
    private val fragments: ArrayList<Fragment> = ArrayList()

    fun add(title: String?, fragment: Fragment?) {
        titles.add(title!!)
        fragments.add(fragment!!)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}