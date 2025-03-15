package com.mobile.apicalljetcompose.ui.theme.network

import com.mobile.apicalljetcompose.ui.theme.api.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://lcproapp1-default-rtdb.firebaseio.com/") // Firebase base URL
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin objects
            .build()
    }

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}
