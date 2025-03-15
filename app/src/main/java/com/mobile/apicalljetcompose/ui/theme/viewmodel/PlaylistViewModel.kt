package com.mobile.apicalljetcompose.ui.theme.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.apicalljetcompose.MainApplication

import com.mobile.apicalljetcompose.ui.theme.data.Playlist
import com.mobile.apicalljetcompose.ui.theme.data.PlaylistSongs
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.database.AppDatabase
import com.mobile.apicalljetcompose.ui.theme.database.SongDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel (application: Application) : AndroidViewModel(application) {




    val dataBase = MainApplication.database.songDao()



    val playlistList: LiveData<List<Playlist>> = dataBase.getAllPlaylist()


    private val _playlistSongs = MutableLiveData<List<PlaylistSongs>>()
    val playlistSongsList: LiveData<List<PlaylistSongs>> = _playlistSongs



    init {
        Log.d("Screen","initplaylist")
    }


    fun deletePlaylist(playlistId: Int){
        viewModelScope.launch {
            dataBase.deletePlaylist(playlistId)
            dataBase.deletePlaylistAllsong(playlistId.toLong())
        }
    }

    fun getPlaylistSongs(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Screen", "Call Songs")
            val songs = dataBase.getPlaylistSongs(playlistId)
            _playlistSongs.postValue(songs)  // Use postValue() since we're in a background thread
        }
    }

    fun removePlaylistSong(playlistId: Long,songId: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                dataBase.removeSongFromPlaylist(playlistId, songId)
                val updatedSongs = dataBase.getPlaylistSongs(playlistId)
                _playlistSongs.postValue(updatedSongs) // Ensure LiveData updates on the main thread
                Log.d("Screen", "PlaylistSongs Remove")
            } catch (e: Exception) {
                Log.e("Screen", "Error removing song: ${e.message}")
            }
        }
    }

    fun createPlayList(playlistName: String){
        viewModelScope.launch {
            dataBase.createPlaylist(Playlist(0,playlistName,0))
        }
    }

}