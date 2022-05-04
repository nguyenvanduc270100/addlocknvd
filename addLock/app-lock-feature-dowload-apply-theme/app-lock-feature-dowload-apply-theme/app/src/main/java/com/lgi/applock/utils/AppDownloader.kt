package com.lgi.applock.utils

import android.util.Log
import com.lgi.applock.App
import com.tonyodev.fetch2.*

class AppDownloader private constructor() {
    private var fetchConfiguration: FetchConfiguration =
        FetchConfiguration.Builder(App.instance).build()

    private var fetch: Fetch = Fetch.Impl.getInstance(fetchConfiguration)

    private object Holder {
        val instance = AppDownloader()
    }

    companion object {
        @JvmStatic
        fun getInstance(): AppDownloader = Holder.instance
    }

    fun enqueueDownloadFile(url: String, filePath: String) {
        val downloadRequest = Request(url, filePath)
        downloadRequest.priority = Priority.HIGH
        downloadRequest.networkType = NetworkType.ALL
        fetch.enqueue(downloadRequest,
            { updatedRequest: Request? -> Log.d("lmao enqueue updated", updatedRequest.toString()) }
        ) { error: Error? -> Log.d("lmao enqueue error", error.toString()) }
    }

    fun addListener(fetchListener: FetchListener) {
        fetch.addListener(fetchListener)
    }

    fun removeListener(fetchListener: FetchListener) {
        fetch.removeListener(fetchListener)
    }
}