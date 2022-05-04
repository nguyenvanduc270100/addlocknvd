package com.bienlongtuan.applocker.fragments.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienlongtuan.applocker.activities.LockAppActivity
import com.bienlongtuan.applocker.adapters.AppLockAdapter
import com.bienlongtuan.applocker.databinding.FragmentHomeBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.fragments.lock_app.LockAppFragment
import com.bienlongtuan.applocker.models.AppLockModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        setDataAll()
        setOnClick()

    }

    fun setDataAll(){
        val listAppLock: ArrayList<AppLockModel> = ArrayList()
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))
        listAppLock.add(AppLockModel("Message", "System Application", ""))

        binding.rvData.adapter = AppLockAdapter(listAppLock)
    }

    fun setOnClick(){
        binding.btnLock.setOnClickListener{
//            val action = HomeFragmentDirections.actionHomeToLockAppFragment()
//            it.findNavController().navigate(action);

            val intent = Intent(context, LockAppActivity::class.java)
            startActivity(intent)
        }
    }
}