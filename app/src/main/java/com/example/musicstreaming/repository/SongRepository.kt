package com.example.musicstreaming.repository

import com.example.musicstreaming.data.SongDao
import com.example.musicstreaming.model.Song
import kotlinx.coroutines.flow.Flow

class SongRepository(private val songDao: SongDao) {

    val allSongs: Flow<List<Song>> = songDao.getAllSongs()

    suspend fun insertSong(song: Song) {
        songDao.insertSong(song)
    }

    suspend fun deleteSong(song: Song) {
        songDao.deleteSong(song)
    }

    suspend fun insertAll(songs: List<Song>) {
        songs.forEach { songDao.insertSong(it) }
    }
}


