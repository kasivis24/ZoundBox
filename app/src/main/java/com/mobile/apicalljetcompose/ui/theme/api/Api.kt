package com.mobile.apicalljetcompose.ui.theme.api
import com.mobile.apicalljetcompose.ui.theme.data.SongsResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("JamMusicAllSongs.json") // Firebase endpoint
    fun getSongs(): Call<SongsResponse>
}
