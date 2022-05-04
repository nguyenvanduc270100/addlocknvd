package com.lgi.applock.fragments

import android.app.ProgressDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.lgi.applock.R
import com.lgi.applock.adapters.AppLockAdapter
import com.lgi.applock.adapters.ViewPagerAdapter
import com.lgi.applock.data.db.DbApp
import com.lgi.applock.models.DBAppLock
import com.lgi.applock.databinding.FragmentHomeBinding
import com.lgi.applock.models.KeySearchEvent
import com.lgi.applock.utils.TabLayoutEnum
import com.lgi.applock.utils.hideKeyboard
import com.lgi.applock.utils.showKeyBoard
import org.greenrobot.eventbus.EventBus

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    lateinit var viewpageradapter: ViewPagerAdapter

    var mListAppLock = ArrayList<DBAppLock>()
    var arrFileSearch = ArrayList<DBAppLock>()
    var adapter: AppLockAdapter? = null
    var dbApp: DbApp? = null
    var showPo = false
    private var progressDialog: ProgressDialog? = null

    var enumCurrent = TabLayoutEnum.all;


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

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> enumCurrent = TabLayoutEnum.all
                    1 -> enumCurrent = TabLayoutEnum.installed
                    2 -> enumCurrent = TabLayoutEnum.system
                    3 -> enumCurrent = TabLayoutEnum.favorite
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

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
                EventBus.getDefault().post(KeySearchEvent(true, s.toString().trim()))
            }
        })
    }


}