package com.bienlongtuan.applocker.activities

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.databinding.ActivityLockAppBinding
import com.bienlongtuan.applocker.databinding.ActivityMainBinding
import com.bienlongtuan.applocker.fragments.BlankFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class LockAppActivity : BaseActivity<ActivityLockAppBinding>() {

    override fun setViewBinding() = ActivityLockAppBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}