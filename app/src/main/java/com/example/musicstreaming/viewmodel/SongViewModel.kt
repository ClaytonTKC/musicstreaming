package com.example.musicstreaming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.repository.SongRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(private val repository: SongRepository) : ViewModel() {

    val songs: StateFlow<List<Song>> = repository.allSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSong(song: Song) {
        viewModelScope.launch {
            repository.insertSong(song)
        }
    }

    fun removeSong(song: Song) {
        viewModelScope.launch {
            repository.deleteSong(song)
        }
    }
}
