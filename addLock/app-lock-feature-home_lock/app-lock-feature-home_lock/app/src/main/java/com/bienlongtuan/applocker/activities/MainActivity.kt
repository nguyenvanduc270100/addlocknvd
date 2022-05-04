package com.bienlongtuan.applocker.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivityMainBinding
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView: SmoothBottomBar = binding.navView
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_tools, R.id.navigation_settings
            )
        )

//        setupActionBarWithNavController(navController, appBarConfiguration)
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu
        navView.setupWithNavController(menu, navController)

       someMethodThatUsesActivity()
    }

    private fun someMethodThatUsesActivity() {
        val decorView: View = this.window.decorView
        var systemUiVisibilityFlags: Int = decorView.systemUiVisibility
        systemUiVisibilityFlags =
            systemUiVisibilityFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        decorView.systemUiVisibility = systemUiVisibilityFlags
    }


}