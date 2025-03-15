package com.mobile.apicalljetcompose.ui.theme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobile.apicalljetcompose.ui.theme.data.CloudSongs
import com.mobile.apicalljetcompose.ui.theme.data.SongsResponse
import com.mobile.apicalljetcompose.ui.theme.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    private val _cloudSongs = MutableLiveData<List<CloudSongs>>()
    val cloudSongs: LiveData<List<CloudSongs>> get() = _cloudSongs

    fun fetchSongs() {
        RetrofitInstance.api.getSongs().enqueue(object : Callback<SongsResponse> {
            override fun onResponse(call: Call<SongsResponse>, response: Response<SongsResponse>) {
                if (response.isSuccessful) {
                    val songList = response.body()?.values?.toList() ?: emptyList()
                    _cloudSongs.postValue(songList)
                }
            }

            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                _cloudSongs.postValue(emptyList()) // Handle errors
            }
        })
    }
}
