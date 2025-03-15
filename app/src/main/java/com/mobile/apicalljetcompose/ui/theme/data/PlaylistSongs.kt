package com.mobile.apicalljetcompose.ui.theme.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlistsongs")
data class PlaylistSongs(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val songId : String,
    val uri: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val playlistId: Long,
)