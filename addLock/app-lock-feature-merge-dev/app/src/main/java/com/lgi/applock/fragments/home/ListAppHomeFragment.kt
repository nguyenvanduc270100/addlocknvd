package com.lgi.applock.fragments.home

import android.content.*
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lgi.applock.adapters.AppLockAdapter
import com.lgi.applock.data.db.DbApp
import com.lgi.applock.data.model.DBAppLock
import com.lgi.applock.databinding.FragmentListAppHomeBinding
import com.lgi.applock.fragments.BaseFragment
import com.lgi.applock.models.KeySearchEvent
import com.lgi.applock.models.TypeActionUpdate
import com.lgi.applock.models.UpdateEvent
import com.lgi.applock.utils.Constants
import com.lgi.applock.utils.TabLayoutEnum
import com.lgi.applock.utils.get_app_utils.GetListApp
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class ListAppHomeFragment(private val enumTab: TabLayoutEnum) :
    BaseFragment<FragmentListAppHomeBinding>() {

    var mListAppLock = ArrayList<DBAppLock>()
    var arrFileSearch = ArrayList<DBAppLock>()
    var adapter: AppLockAdapter? = null
    var dbApp: DbApp? = null

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListAppHomeBinding {
        return FragmentListAppHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        if (enumTab == TabLayoutEnum.favorite) binding.viewFastOptions.visibility = View.VISIBLE
        else binding.viewFastOptions.visibility = View.GONE

        dbApp = DbApp(requireContext(), null)
        setRecycleView()
        setSwitch()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GetListApp(requireContext(), this).execute()
    }

    private fun setRecycleView() {
        adapter =
            AppLockAdapter(requireContext(), object : AppLockAdapter.ItemListener {
                override fun onClickLock(position: Int, packageApp: DBAppLock) {
                    mListAppLock[position].isLock = packageApp.isLock
                    dbApp!!.updateLock(packageApp.packagename, packageApp.isLock)
                }

                override fun onClickFavorite(position: Int, packageApp: DBAppLock) {
                    if (enumTab == TabLayoutEnum.favorite) {
                        dbApp!!.updateFavorite(packageApp.packagename, 0)
                        mListAppLock.removeAt(position)
                        adapter?.notifyItemRemoved(position)
                        adapter?.notifyItemRangeChanged(0, mListAppLock.size)
                    } else {
                        mListAppLock[position].isFavorite = packageApp.isFavorite
                        dbApp!!.updateFavorite(packageApp.packagename, packageApp.isFavorite)
                    }

                }

            })
        binding.rvData.layoutManager = LinearLayoutManager(context)
        binding.rvData.setItemViewCacheSize(20)
        binding.rvData.adapter = adapter
    }

    private fun sortDataApp(list: ArrayList<DBAppLock>) {
        mListAppLock = when (enumTab) {
            TabLayoutEnum.installed -> {
                list.filter {
                    (it.flags.and(ApplicationInfo.FLAG_SYSTEM) != Constants.APP_INSTALL)
                } as ArrayList<DBAppLock>
            }
            TabLayoutEnum.system -> {
                list.filter {
                    (it.flags.and(ApplicationInfo.FLAG_SYSTEM) != Constants.APP_SYSTEM)
                } as ArrayList<DBAppLock>
            }
            else -> {
                list
            }
        }
        adapter?.setData(mListAppLock)
    }


    private fun isListFavorite(dbApp: DbApp): Boolean {
        val a = dbApp.getFavorite(true).size
        val b = dbApp.getFavorite(false).size
        return a == b
    }

    private fun setSwitch() {
        dbApp?.let { db ->
            binding.btnSwitch.isChecked = isListFavorite(db)
            binding.btnSwitch.setOnClickListener{
                if (binding.btnSwitch.isChecked) {
                    db.updateAllFavoriteLock(1)
                } else {
                    db.updateAllFavoriteLock(0)
                }
                binding.btnSwitch.isChecked = isListFavorite(db)
                mListAppLock = db.getFavorite(true)
                sortDataApp(mListAppLock)
                EventBus.getDefault().post(UpdateEvent(TypeActionUpdate.Lock, null, true))
            }
        }

    }


    fun updateData(list: ArrayList<DBAppLock>) {
        sortDataApp(list)
    }


    fun onPost() {

    }

    fun onPre() {
    }

    fun onUp(vararg values: Int?) {
    }

    fun onDoing(list: ArrayList<DBAppLock>): ArrayList<DBAppLock> {
        arrFileSearch.clear()
        if (dbApp!!.getApp().size == 0) {
            for (app in list)
                dbApp!!.insertApp(
                    app.name,
                    app.packagename,
                    app.isFavorite,
                    app.isLock,
                    app.flags
                )

        }
        if (list.size > dbApp!!.getApp().size) {
            for (app in list) {
                if (!dbApp!!.checkPath(app.packagename)) {
                    dbApp!!.insertApp(
                        app.name,
                        app.packagename,
                        app.isFavorite,
                        app.isLock,
                        app.flags

                    )
                }
            }
        }
        if (list.size < dbApp!!.getApp().size) {
            for (app in dbApp!!.getApp()) {
                if (!checkPathNho(list, app.packagename)) {
                    dbApp!!.deleteApp(app.packagename)
                }

            }

        }


        if (dbApp!!.getApp().size > 0) {
            mListAppLock = if (enumTab == TabLayoutEnum.favorite) {
                dbApp!!.getFavorite()
            } else dbApp!!.getApp()
        }
        return mListAppLock

    }

    private fun checkPathNho(list: ArrayList<DBAppLock>, pagkageName: String): Boolean {
        for (app in list) {
            if (pagkageName == app.packagename) return true
        }
        return false

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTaskSearchSelectEvent(event: KeySearchEvent) {
        if (event.isSearch && event.keySearch.isNotEmpty()) {
            mListAppLock =
                mListAppLock.filter {
                    it.name.lowercase().contains(event.keySearch.lowercase())
                } as ArrayList<DBAppLock>

        } else {
            dbApp?.let {
                if (it.getApp().size > 0) {
                    if (enumTab == TabLayoutEnum.favorite) {
                        binding.btnSwitch.isChecked = isListFavorite(it)
                        mListAppLock = it.getFavorite(true)
                    } else mListAppLock = it.getApp()
                }
            }
        }
        sortDataApp(mListAppLock)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateFavoriteEvent(event: UpdateEvent) {
        if (enumTab != TabLayoutEnum.favorite && event.updateSwitch == true) {
            Log.e("onUpdateFavoriteEvent","${event.isUpdate}")
            dbApp?.let {
                mListAppLock = it.getApp()
                sortDataApp(mListAppLock)
            }
            return
        }

        if ((requireParentFragment() as HomeFragment).enumCurrent != enumTab) {
            Log.e("onUpdateFavoriteEvent 1","${event.isUpdate}")
            if (enumTab == TabLayoutEnum.favorite) {
                Log.e("onUpdateFavoriteEvent 2","${event.isUpdate}")
                dbApp?.let {
                    binding.btnSwitch.isChecked = isListFavorite(it)
                    mListAppLock = it.getFavorite(true)
                }
            } else {
                Log.e("onUpdateFavoriteEvent 2","${event.isUpdate}")
                event.model?.let {
                    for (item in mListAppLock) {
                        if (item.packagename == it.packagename) {
                            item.isFavorite = it.isFavorite
                            item.isLock = it.isLock
                        }
                    }
                }
            }
            sortDataApp(mListAppLock)
        }

    }
}