package com.mobile.apicalljetcompose.ui.theme.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.Playlist
import com.mobile.apicalljetcompose.ui.theme.data.PlaylistSongs
import com.mobile.apicalljetcompose.ui.theme.data.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song)

    @Query("SELECT * FROM songs")
    fun getAllSongs(): List<Song>

    @Delete
    suspend fun delete(song: Song)

    // CreatePlaylist
    @Insert
    suspend fun createPlaylist(playlist: Playlist)

    // Get All Playlists
    @Query("SELECT * FROM playlists")
    fun getAllPlaylist(): LiveData<List<Playlist>>

    // Insert The Song in Playlist
    @Insert
    suspend fun putTheSongInPlaylist(playlistSongs: PlaylistSongs)

    // Update PlaylistSongCount \
    @Query("UPDATE playlists SET songsCount = songsCount + 1 WHERE id = :playlistId")
    suspend fun updateSongCount(playlistId: Int)

    // Delete Playlist and Song in playlist
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    // Delete AllSong From Playlist
    @Query("DELETE FROM PLAYLISTSONGS WHERE playlistId = :playlistId")
    suspend fun deletePlaylistAllsong(playlistId: Long)

    // Get The PlaylistSongs
    @Query("SELECT * FROM playlistsongs WHERE playlistId = :playlistId")
    suspend fun getPlaylistSongs(playlistId: Long): List<PlaylistSongs>

    // Delete Song From PlaylistSongs
    @Query("DELETE FROM PLAYLISTSONGS WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long,songId: String)

    // AddToFavorite
    @Insert
    suspend fun addToFavorite(favorite: Favorite)

    // Check For Avoid Duplicate
    @Query("SELECT COUNT(*) FROM favorite WHERE songId = :id")
    suspend fun isAlredyInFav(id: String) : Long

    // Get All Favorite
    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): LiveData<List<Favorite>>

    // Remove In Favorite
    @Query("DELETE FROM favorite WHERE songId = :id")
    suspend fun removeSongFromFav(id: String)
}
