package com.mobile.apicalljetcompose.ui.theme.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.Playlist
import com.mobile.apicalljetcompose.ui.theme.data.PlaylistSongs
import com.mobile.apicalljetcompose.ui.theme.data.Song

@Database(entities = [Song::class,Playlist::class,Favorite::class,PlaylistSongs::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    companion object {
        const val DATA_NAME = "music_database"
    }
}
