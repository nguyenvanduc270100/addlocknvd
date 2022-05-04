package com.lgi.applock.activities

import android.content.Intent
import android.os.Bundle
import com.lgi.applock.databinding.ActivityMainBinding
import com.lgi.applock.utils.PinEncoderUtils
import com.lgi.applock.layouts.MainActivityTabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lgi.applock.R
import com.lgi.applock.adapters.MainActivityViewpagerAdapter

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PinEncoderUtils.checkIfPinCodeExisted(this@MainActivity)) {
            startActivity(Intent(this@MainActivity, SetPinActivity::class.java))
        }
        initViewpagerContents()
    }

    private fun initViewpagerContents() = with(binding) {
        vp2TabsContainer.adapter = MainActivityViewpagerAdapter(this@MainActivity)
        vp2TabsContainer.isUserInputEnabled = false
        TabLayoutMediator(vp2TabsLayout, vp2TabsContainer) { _, _ -> }.attach()
        vp2TabsLayout.getTabAt(0)?.customView = MainActivityTabItem.create(
            this@MainActivity,
            getString(R.string.main_activity_home_tab),
            R.drawable.ic_tab_home,
            true
        )
        vp2TabsLayout.getTabAt(1)?.customView = MainActivityTabItem.create(
            this@MainActivity,
            getString(R.string.main_activity_theme_tab),
            R.drawable.ic_tab_theme,
            false
        )
        vp2TabsLayout.getTabAt(2)?.customView = MainActivityTabItem.create(
            this@MainActivity,
            getString(R.string.main_activity_settings_tab),
            R.drawable.ic_tab_setting,
            false
        )
        vp2TabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                (tab.customView as MainActivityTabItem).setActive(true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                (tab.customView as MainActivityTabItem).setActive(false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                (tab.customView as MainActivityTabItem).setActive(true)
            }
        })
    }
}