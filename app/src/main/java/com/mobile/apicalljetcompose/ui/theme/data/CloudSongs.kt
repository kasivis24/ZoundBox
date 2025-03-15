package com.mobile.apicalljetcompose.ui.theme.data

data class CloudSongs(
    val artistName: String = "",
    val songTitle: String = "",
    val songUri: String = "",
    val songId: String = "",
)

typealias SongsResponse = Map<String, CloudSongs>
