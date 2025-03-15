package com.mobile.apicalljetcompose.ui.theme.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.apicalljetcompose.ui.theme.data.CloudSongs
import com.mobile.apicalljetcompose.ui.theme.data.Song
import com.mobile.apicalljetcompose.ui.theme.repository.Repository
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {
    private val repository = Repository()
    val songs: LiveData<List<CloudSongs>> get() = repository.cloudSongs
    var searchQuery = mutableStateOf("")
    val filteredSongs = MediatorLiveData<List<CloudSongs>>().apply {
        addSource(songs) { updateFilteredSongs() }
    }


    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        viewModelScope.launch {
            repository.fetchSongs()
        }
    }

    fun updateFilteredSongs() {
        val query = searchQuery.value.lowercase()
        filteredSongs.value = songs.value?.filter {
            it.songTitle.lowercase().contains(query) ||
                    it.artistName.lowercase().contains(query)
        } ?: emptyList()



    }
}
