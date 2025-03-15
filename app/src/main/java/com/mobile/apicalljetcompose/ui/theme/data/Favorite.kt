package com.mobile.apicalljetcompose.ui.theme.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val songId: String,
    val title: String,
    val uri: String,
    val artist: String,
    val duration: Long
)