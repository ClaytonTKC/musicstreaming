package com.example.musicstreaming.repository

import com.example.musicstreaming.data.PlaylistDao
import com.example.musicstreaming.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val playlistDao: PlaylistDao) {

    val allPlaylists: Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist)
    }

    suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist)
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlist)
    }
}
