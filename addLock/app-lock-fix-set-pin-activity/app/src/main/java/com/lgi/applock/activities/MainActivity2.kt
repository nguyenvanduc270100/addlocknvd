package com.lgi.applock.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.lgi.applock.R
import com.lgi.applock.databinding.ActivityMain2Binding
import com.lgi.applock.services.AppLockerService
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar


@AndroidEntryPoint
class MainActivity2 : BaseActivity<ActivityMain2Binding>() {

    override fun setViewBinding() = ActivityMain2Binding.inflate(layoutInflater)

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
        val serviceIntent = Intent(this, AppLockerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(serviceIntent)
        } else {
            this.startService(serviceIntent)
        }

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
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}