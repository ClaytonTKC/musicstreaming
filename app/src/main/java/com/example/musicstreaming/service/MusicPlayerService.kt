package com.example.musicstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstreaming.model.Song
import androidx.media3.common.MediaItem



class MusicPlayerService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private var currentSong: Song? = null
    private var isShuffling = false
    private var isLooping = false
    private var songQueue: MutableList<Song> = mutableListOf()
    private var currentIndex = 0

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun setQueue(songs: List<Song>, startIndex: Int = 0) {
        songQueue = songs.toMutableList()
        currentIndex = startIndex
        playCurrent()
    }

    fun playSong(song: Song) {
        currentSong = song
        val mediaItem = MediaItem.fromUri(song.filePath)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun playCurrent() {
        if (songQueue.isNotEmpty()) {
            playSong(songQueue[currentIndex])
        }
    }

    fun playNext() {
        if (songQueue.isEmpty()) return
        currentIndex = if (isShuffling) {
            (songQueue.indices - currentIndex).random()
        } else {
            (currentIndex + 1) % songQueue.size
        }
        playCurrent()
    }

    fun playPrevious() {
        if (songQueue.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) songQueue.lastIndex else currentIndex - 1
        playCurrent()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    fun isPlaying(): Boolean = exoPlayer?.isPlaying == true

    fun toggleLoop() {
        isLooping = !isLooping
        exoPlayer?.repeatMode = if (isLooping) ExoPlayer.REPEAT_MODE_ONE else ExoPlayer.REPEAT_MODE_OFF
    }

    fun toggleShuffle() {
        isShuffling = !isShuffling
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
}
