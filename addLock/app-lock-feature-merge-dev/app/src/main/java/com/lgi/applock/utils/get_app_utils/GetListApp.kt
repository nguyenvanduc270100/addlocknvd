package com.lgi.applock.utils.get_app_utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import com.lgi.applock.data.model.DBAppLock
import com.lgi.applock.fragments.home.ListAppHomeFragment
import kotlin.collections.ArrayList


class GetListApp(
    private val context: Context,
    private var fragment: ListAppHomeFragment
) : AsyncTask<Void, Int, ArrayList<DBAppLock>>() {
    override fun doInBackground(vararg p0: Void?): ArrayList<DBAppLock> {
        val list = getListOfInstalledApp(context)
        return fragment.onDoing(list)
    }

    private fun getListOfInstalledApp(context: Context): ArrayList<DBAppLock> {
        val packageManager = context.packageManager
        val installedApps =
            ArrayList<DBAppLock>()
        val apps =
            packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        if (apps.isNotEmpty()) {
            for (i in apps.indices) {
                val p = apps[i]
                try {
                    if (null != packageManager.getLaunchIntentForPackage(p.packageName)) {
                        val isLock = if (p.packageName == context.packageName) {
                            1
                        } else {
                            0
                        }
                        val app = DBAppLock(
                            p.applicationInfo.loadLabel(packageManager).toString(),
                            p.packageName,
                            p.applicationInfo.flags,
                            isLock,
                            0
                        )

                        installedApps.add(app)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        return installedApps
    }

    override fun onPostExecute(result: ArrayList<DBAppLock>) {
        super.onPostExecute(result)
        fragment.onPost()
        fragment.updateData(result)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        fragment.onPre()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        fragment.onUp(*values)
    }


}



