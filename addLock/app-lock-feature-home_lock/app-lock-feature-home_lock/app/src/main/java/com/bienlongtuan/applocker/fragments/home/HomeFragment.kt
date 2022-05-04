package com.bienlongtuan.applocker.fragments.home

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.bienlongtuan.applocker.adapters.ViewPagerAdapter
import com.bienlongtuan.applocker.databinding.FragmentHomeBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.fragments.lock_app.LockAppFragment
import com.bienlongtuan.applocker.models.AppLockModel
import com.bienlongtuan.applocker.models.KeySearchEvent
import com.bienlongtuan.applocker.utils.Constants
import org.greenrobot.eventbus.EventBus
import android.view.inputmethod.InputMethodManager as InputMethodManager1

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    lateinit var viewpageradapter: ViewPagerAdapter

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
        viewpageradapter.add(context?.resources?.getString(R.string.tab_home_all), ListAppHomeFragment(false))
        viewpageradapter.add(context?.resources?.getString(R.string.tab_home_downloaded), ListAppHomeFragment(false))
        viewpageradapter.add(context?.resources?.getString(R.string.tab_home_system), ListAppHomeFragment(false))
        viewpageradapter.add(context?.resources?.getString(R.string.tab_home_favorited), ListAppHomeFragment(true))

        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.adapter = viewpageradapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setOnClick(){
        binding.toolBarHome.btnLock.setOnClickListener{
//            val action = HomeFragmentDirections.actionHomeToLockAppFragment()
//            it.findNavController().navigate(action);
            val intent = Intent(context, LockAppActivity::class.java)
            startActivity(intent)
        }

        binding.toolBarHome.btnSearch.setOnClickListener{
            binding.toolBarHome.root.visibility = View.GONE
            binding.toolBarSearch.root.visibility = View.VISIBLE
        }

        binding.toolBarSearch.btnClose.setOnClickListener {
            binding.toolBarHome.root.visibility = View.VISIBLE
            binding.toolBarSearch.root.visibility = View.GONE
            EventBus.getDefault().post(KeySearchEvent(false,""))
            binding.toolBarSearch.edtSearch.text = null
            hideKeyboard()
        }
    }

    private fun searchApp(){

        binding.toolBarSearch.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                EventBus.getDefault().post(KeySearchEvent(true,s.toString()))
            }
        })
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager1
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}