package com.bienlongtuan.applocker.fragments.home

import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bienlongtuan.applocker.adapters.AppLockAdapter
import com.bienlongtuan.applocker.data.db.DbApp
import com.bienlongtuan.applocker.data.model.DBAppLock
import com.bienlongtuan.applocker.databinding.FragmentListAppHomeBinding
import com.bienlongtuan.applocker.fragments.BaseFragment
import com.bienlongtuan.applocker.models.KeySearchEvent
import com.bienlongtuan.applocker.models.UpdateEvent
import com.bienlongtuan.applocker.utils.Constants
import com.bienlongtuan.applocker.utils.TabLayoutEnum
import com.bienlongtuan.applocker.utils.get_app_utils.GetListApp
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class ListAppHomeFragment(private val enumTab: TabLayoutEnum) :
    BaseFragment<FragmentListAppHomeBinding>() {

    var mListAppLock = ArrayList<DBAppLock>()
    var sharedPreference: SharedPreferences? = null
    var arrFileSearch = ArrayList<DBAppLock>()
    var adapter: AppLockAdapter? = null
    var dbApp: DbApp? = null
    var showPo = false
    private var progressDialog: ProgressDialog? = null

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListAppHomeBinding {
        createProgress()
        return FragmentListAppHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateOurView() {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        sharedPreference =
            context?.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        if (enumTab == TabLayoutEnum.favorite) binding.viewFastOptions.visibility = View.VISIBLE
        else binding.viewFastOptions.visibility = View.GONE
        setRecycleView(mListAppLock)
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

    private fun createProgress() {
        val intent = IntentFilter()
        intent.addAction("SEARCH")
        intent.addAction("LOCKED")
        intent.addAction("POPUP")
        intent.addAction("DARK")
        requireContext().registerReceiver(broadcastReceiver, intent)
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCancelable(true)
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.max = 100
        dbApp = DbApp(requireContext(), null)
    }

    private fun setRecycleView(mListAppLock: ArrayList<DBAppLock>) {
        adapter =
            AppLockAdapter(requireContext(), object : AppLockAdapter.ItemListener {

                override fun onClickLock(position: Int, packageApp: DBAppLock) {
                    if (mListAppLock[position].isLock == 0) {
                        mListAppLock[position].isLock = 1
                        dbApp!!.updateLock(packageApp.packagename, 1)
                    } else {
                        mListAppLock[position].isLock = 0
                        dbApp!!.updateLock(packageApp.packagename, 0)
                    }

                    val intent = Intent("LOCKED")
                    context!!.sendBroadcast(intent)
                }

                override fun onClickFavorite(position: Int, packageApp: DBAppLock) {
                    if (mListAppLock[position].isFavorite == 0) {
                        mListAppLock[position].isFavorite = 1
                        dbApp!!.updateFavorite(packageApp.packagename, 1)
                    } else {
                        mListAppLock[position].isFavorite = 0
                        dbApp!!.updateFavorite(packageApp.packagename, 0)
                    }
                }

            })
        adapter?.setData(mListAppLock)
        binding.rvData.adapter = adapter
    }

    private fun setSwitch() {
        binding.btnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (item in mListAppLock) {
                    if (item.isLock == 0) {
                        item.isLock = 1
                        dbApp!!.updateLock(item.packagename, 1)
                    }
                }
            } else {
                for (item in mListAppLock) {
                    if (item.isLock == 1) {
                        item.isLock = 0
                        dbApp!!.updateLock(item.packagename, 0)
                    }
                }
            }
            adapter?.notifyDataSetChanged()
            EventBus.getDefault().post(UpdateEvent(true))
        }
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            val action = p1?.action
            val string = p1?.extras?.getString("string")

            when {
            }

        }
    }

    fun updateData(list: ArrayList<DBAppLock>) {
        binding.rvData.layoutManager = LinearLayoutManager(context)
        setRecycleView(list)

    }


    fun onPost() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()

        }


    }

    fun onPre() {
        progressDialog!!.show()

    }

    fun onUp(vararg values: Int?) {
        progressDialog!!.progress = values[0]!!.inv()
    }

    fun onDoing(list: ArrayList<DBAppLock>): ArrayList<DBAppLock> {
        arrFileSearch.clear()
        if (dbApp!!.getApp().size == 0) {
            for (app in list)
                dbApp!!.insertApp(
                    app.name,
                    app.packagename,
                    app.isFavorite,
                    app.isLock
                )

        }
        if (list.size > dbApp!!.getApp().size) {
            for (app in list) {
                if (!dbApp!!.checkPath(app.packagename)) {
                    dbApp!!.insertApp(
                        app.name,
                        app.packagename,
                        app.isFavorite,
                        app.isLock
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
            if (enumTab == TabLayoutEnum.favorite) {
                for (item in dbApp!!.getApp()) {
                    if (item.isFavorite == 1) mListAppLock.add(item)
                }
            } else mListAppLock = dbApp!!.getApp()
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
        if (event.isSearch) {
            val listSearch: ArrayList<DBAppLock> = ArrayList()
            for (item in mListAppLock) {
                if (item.name.lowercase().contains(event.keySearch.lowercase())) {
                    listSearch.add(item)
                }
                adapter?.setData(listSearch)
                adapter?.notifyDataSetChanged()
            }
        } else {
            adapter?.setData(mListAppLock)
            adapter?.notifyDataSetChanged()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateFavoriteEvent(event: UpdateEvent) {
        if(event.isUpdate) {
            if (dbApp!!.getApp().size > 0 ) {
                mListAppLock.clear()
                if(enumTab == TabLayoutEnum.favorite) {
                    for (item in dbApp!!.getApp()) {
                        if (item.isFavorite == 1) mListAppLock.add(item)
                    }
                }else mListAppLock = dbApp!!.getApp()
                adapter?.setData(mListAppLock)
                adapter?.notifyDataSetChanged()
            }
        }
    }
}