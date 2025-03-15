import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.mobile.apicalljetcompose.MainApplication
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(application).build()

    private val mediaItemList = mutableListOf<MediaItem>()

    val database = MainApplication.database.songDao()


    // ✅ StateFlow for current song title
    private val _currentSong = MutableStateFlow<String>("")
    val currentSong: StateFlow<String> = _currentSong.asStateFlow()

    private val _currentArtist = MutableStateFlow<String>("")
    val currentArtist: StateFlow<String> = _currentArtist.asStateFlow()

    private val _songId = MutableStateFlow<String>("")
    val songId: StateFlow<String> = _songId.asStateFlow()

    private val _songUri = MutableStateFlow<String>("")
    val songUri: StateFlow<String> = _songUri.asStateFlow()

    // ✅ StateFlow for playing state (true = playing, false = paused)
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _totalDuration = MutableStateFlow(0L)
    val totalDuration: StateFlow<Long> = _totalDuration


    private val _isFav = MutableStateFlow(false)
    val isFav: StateFlow<Boolean> = _isFav.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()


    private val mediaSession: MediaSession

    init {
        Log.d("Screen", "PlayerViewModel Initialized")
        startPositionUpdater()

        mediaSession = MediaSession.Builder(application, exoPlayer).build()

        // ✅ Add listener to track playback changes
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                viewModelScope.launch {
                   try {
                       _isFav.value = database.isAlredyInFav(mediaItem?.mediaMetadata?.albumArtist.toString() ?: "") > 0
                       _currentSong.value = mediaItem?.mediaMetadata?.title?.toString() ?: "Unknown Song"
                       _currentArtist.value = mediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown Artist"
                       _songId.value = mediaItem?.mediaMetadata?.albumArtist?.toString() ?: ""
                       _songUri.value = mediaItem?.mediaMetadata?.genre?.toString() ?: "Unknown SongUri"
                   }catch (e:Exception){
                       Log.d("Screen",e.toString() +" ")
                   }
                }
            }


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                viewModelScope.launch {
                    _isPlaying.value = isPlaying
                }
            }


        })
    }






    fun setPlayList(songList : List<SongData>){
        Log.d("Screen","PlayerSet init")
        try {
            exoPlayer.clearMediaItems()
            mediaItemList.clear()
            for (data in songList) {
                val mediaMetadata = androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(data.name)
                    .setArtist(data.artist)
                    .setGenre(data.song)
                    .setAlbumArtist(data.id)
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(data.song)
                    .setMediaMetadata(mediaMetadata)
                    .build()

                mediaItemList.add(mediaItem)
            }
            Log.d("Screen","PlayerSet ${mediaItemList}")
            exoPlayer.setMediaItems(mediaItemList)
            exoPlayer.prepare()
        }
        catch (e:Exception){
            Log.d("Screen","$e")
        }
    }

    fun shuffle(){
        _isShuffle.value = !_isShuffle.value
        exoPlayer.shuffleModeEnabled = _isShuffle.value
    }



    @OptIn(UnstableApi::class)
    fun next(){
        exoPlayer.next()
    }

    @OptIn(UnstableApi::class)
    fun previous(){
        exoPlayer.previous()
    }


    fun playAtIndex(index: Int){
        exoPlayer.seekTo(index,0)
        exoPlayer.play()
    }


    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }


    fun resume(){
        if (exoPlayer.isPlaying){
            exoPlayer.pause()
        }else{

            exoPlayer.play()
        }
    }


    private fun startPositionUpdater() {
        viewModelScope.launch {
            while (true) {
                _isFav.value = database.isAlredyInFav(exoPlayer.currentMediaItem?.mediaMetadata?.albumArtist.toString() ?: "") > 0
                Log.d("Screen",_isFav.value.toString() + exoPlayer.currentMediaItem?.mediaMetadata?.albumArtist.toString())
                _currentPosition.value = exoPlayer.currentPosition
                _totalDuration.value = exoPlayer.duration
                _isPlaying.value = exoPlayer.isPlaying
                delay(500) // Update every 500ms
            }
        }
    }


    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes min $seconds sec"
    }

    fun stop() {
        exoPlayer.stop()
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
