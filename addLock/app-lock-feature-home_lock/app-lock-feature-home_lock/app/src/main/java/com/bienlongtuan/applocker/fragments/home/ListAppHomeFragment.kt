package com.bienlongtuan.applocker.fragments.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienlongtuan.applocker.activities.LockAppActivity
import com.bienlongtuan.applocker.adapters.AppLockAdapter
import com.bienlongtuan.applocker.databinding.FragmentHomeBinding
import com.bienlongtuan.applocker.databinding.FragmentListAppHomeBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.fragments.lock_app.LockAppFragment
import com.bienlongtuan.applocker.models.AppLockModel
import com.bienlongtuan.applocker.models.KeySearchEvent
import com.bienlongtuan.applocker.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListAppHomeFragment(private val isFavoriteFragment: Boolean) : BaseFragment<FragmentListAppHomeBinding>(), AppLockAdapter.OnClickListener{

    var adapter: AppLockAdapter?= null
    private var listAppLock: ArrayList<AppLockModel> = ArrayList()
    var sharedPreference: SharedPreferences? = null

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListAppHomeBinding {
        return FragmentListAppHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        sharedPreference =  context?.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        setDataAll()
    }

    override fun onResume() {
        super.onResume()
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun setDataAll(){
        if(!isFavoriteFragment) {
            listAppLock.add(
                AppLockModel(
                    "Message", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Instagram", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "FaceBook", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Zalo", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Call", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Zing mp3", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Mocha", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Telegram", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "YouTube", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )
            listAppLock.add(
                AppLockModel(
                    "Chrome", "System Application", "",
                    isFavorite = false,
                    isLock = false
                )
            )


        }else {
            val stringListFavorite = sharedPreference?.getString(Constants.LIST_FAVORITE, "")
            if(stringListFavorite?.isEmpty() == false) listAppLock = Gson().fromJson(
                stringListFavorite,
                object : TypeToken<List<AppLockModel?>?>() {}.type
            )

        }

        adapter = context?.let { AppLockAdapter(it, this) }
        binding.rvData.adapter = adapter
        adapter?.setData(listAppLock)
        adapter!!.notifyDataSetChanged()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTaskSearchSelectEvent(event: KeySearchEvent) {
        if(event.isSearch){
            val listSearch: ArrayList<AppLockModel> = ArrayList()
            for (item in listAppLock){
                if(item.name.lowercase().contains(event.keySearch.lowercase())){
                    listSearch.add(item)
                }
                adapter?.setData(listSearch)
                adapter?.notifyDataSetChanged()
            }
        }else {
            adapter?.setData(listAppLock)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onClickFavorite(position:Int, isFavorite: Boolean) {
        adapter?.notifyItemChanged(position)
    }

    override fun onClickLock(position:Int, isLock: Boolean) {
        adapter?.notifyItemChanged(position)
    }

}