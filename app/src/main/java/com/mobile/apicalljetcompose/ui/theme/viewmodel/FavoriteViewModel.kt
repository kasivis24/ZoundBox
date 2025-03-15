package com.mobile.apicalljetcompose.ui.theme.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.apicalljetcompose.MainApplication
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel(){

    val database = MainApplication.database.songDao()

    val favoriteList : LiveData<List<Favorite>> = database.getAllFavorite()


    init {
        Log.d("Screen","Favoriteviewmodel")
    }


    fun removeSongFromFav(songId: String){
        viewModelScope.launch (Dispatchers.IO){
            database.removeSongFromFav(songId)
        }
    }

}