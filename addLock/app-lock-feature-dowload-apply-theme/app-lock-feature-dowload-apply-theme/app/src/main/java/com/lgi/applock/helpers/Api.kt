package com.lgi.applock.helpers

import com.lgi.applock.models.Image
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/images/list")
    fun getListTheme() : Call<List<Image>>
}