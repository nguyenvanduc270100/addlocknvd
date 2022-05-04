package com.bienlongtuan.applocker.fragments.home

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienlongtuan.applocker.App
import com.bienlongtuan.applocker.R
import com.bienlongtuan.applocker.activities.LockAppActivity
import com.bienlongtuan.applocker.adapters.AppLockAdapter
import com.bienlongtuan.applocker.data.db.DbApp
import com.bienlongtuan.applocker.data.model.DBAppLock
import com.bienlongtuan.applocker.adapters.ViewPagerAdapter
import com.bienlongtuan.applocker.databinding.FragmentHomeBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.models.AppLockModel
import com.bienlongtuan.applocker.utils.get_app_utils.GetListApp
import dagger.hilt.android.AndroidEntryPoint
import com.bienlongtuan.applocker.models.KeySearchEvent
import com.bienlongtuan.applocker.utils.*
import org.greenrobot.eventbus.EventBus
import android.view.inputmethod.InputMethodManager as InputMethodManager1

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    lateinit var viewpageradapter: ViewPagerAdapter

    var mListAppLock = ArrayList<DBAppLock>()
    var arrFileSearch = ArrayList<DBAppLock>()
    var adapter: AppLockAdapter? = null
    var dbApp: DbApp? = null
    var showPo = false
    private var progressDialog: ProgressDialog? = null

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        setOnClick()
        searchApp()

        viewpageradapter = ViewPagerAdapter(childFragmentManager)
        viewpageradapter.add(
            context?.resources?.getString(R.string.tab_home_all),
            ListAppHomeFragment(TabLayoutEnum.all)
        )
        viewpageradapter.add(
            context?.resources?.getString(R.string.tab_home_downloaded),
            ListAppHomeFragment(TabLayoutEnum.installed)
        )
        viewpageradapter.add(
            context?.resources?.getString(R.string.tab_home_system),
            ListAppHomeFragment(TabLayoutEnum.system)
        )
        viewpageradapter.add(
            context?.resources?.getString(R.string.tab_home_favorited),
            ListAppHomeFragment(TabLayoutEnum.favorite)
        )

        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.adapter = viewpageradapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    private fun setOnClick() {
        binding.toolBarHome.btnSearch.setOnClickListener {
            binding.toolBarHome.root.visibility = View.GONE
            binding.toolBarSearch.root.visibility = View.VISIBLE
            context?.showKeyBoard(requireView())
        }

        binding.toolBarSearch.btnClose.setOnClickListener {
            binding.toolBarHome.root.visibility = View.VISIBLE
            binding.toolBarSearch.root.visibility = View.GONE
            EventBus.getDefault().post(KeySearchEvent(false, ""))
            binding.toolBarSearch.edtSearch.text = null
            context?.hideKeyboard(requireView())
        }
    }

    private fun searchApp() {

        binding.toolBarSearch.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                EventBus.getDefault().post(KeySearchEvent(true, s.toString()))
            }
        })
    }


}