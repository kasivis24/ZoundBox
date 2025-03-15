package com.mobile.apicalljetcompose.ui.theme.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.apicalljetcompose.MainApplication
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.PlaylistSongs
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongViewModel : ViewModel(){

    val dataBase = MainApplication.database.songDao()
    private val _songList = MutableStateFlow<List<SongData>>(emptyList())
    val songList = _songList.asStateFlow()


    private val _currentSongItemfav = MutableStateFlow(false)
    val currentSongItemfav: StateFlow<Boolean> = _currentSongItemfav.asStateFlow()



    init {
        Log.d("Screen","songviewmodel")
    }


    fun addTheSongToPlaylists(selectedPlaylist: List<Long>, songData: MutableState<SongData>){
        for (playlistId in selectedPlaylist){
            viewModelScope.launch(Dispatchers.IO){
                dataBase.updateSongCount(playlistId.toInt())
                dataBase.putTheSongInPlaylist(PlaylistSongs(0,songData.value.id,songData.value.song,songData.value.name,songData.value.artist,songData.value.duration,playlistId))
            }
        }
    }



    fun currentItemSongIsFav(songId: String){
        viewModelScope.launch(Dispatchers.IO){
            val result = dataBase.isAlredyInFav(songId).toInt() == 0
            _currentSongItemfav.value = result
        }
    }

    fun removeSongFromFav(songId: String){
        viewModelScope.launch (Dispatchers.IO){
            dataBase.removeSongFromFav(songId)
        }
    }



    fun addToFavorite(favorite: Favorite,songId: String){
        Log.d("Screen","isfav")
        Log.d("Screen","isfav")

        viewModelScope.launch(Dispatchers.IO){
            val isFav = dataBase.isAlredyInFav(songId).toInt() == 0

            if (isFav){
                dataBase.addToFavorite(favorite)
            }else{
                Log.d("Screen","allready in fav")
            }

        }
    }




    fun fetchAudio(context: Context) {
        Log.d("Screen","songviewmodelFetch")
        viewModelScope.launch (Dispatchers.IO){
            val audioFiles = getAudioFiles(context)
            _songList.value = audioFiles
        }
    }

     private fun getAudioFiles(context: Context): List<SongData> {
        val songList = mutableListOf<SongData>()

        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
        )
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)

                Log.d("Screen",contentUri.toString())
                songList.add(SongData(id.toString(),title,contentUri.toString(),artist,duration))

            }
        }
        return songList
    }
}