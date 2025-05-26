package com.example.musicstreaming

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.musicstreaming.data.MusicDatabase
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.repository.PlaylistRepository
import com.example.musicstreaming.repository.SongRepository
import com.example.musicstreaming.ui.navigation.AppNavHost
import com.example.musicstreaming.ui.navigation.NavRoutes
import com.example.musicstreaming.ui.theme.MusicStreamingTheme
import com.example.musicstreaming.viewmodel.PlaylistViewModel
import com.example.musicstreaming.viewmodel.SongViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = MusicDatabase.getDatabase(this)
        val songRepo = SongRepository(db.songDao())
        val playlistRepo = PlaylistRepository(db.playlistDao())
        val songViewModel = SongViewModel(songRepo)
        val playlistViewModel = PlaylistViewModel(playlistRepo)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                loadSongsFromDevice(songRepo)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                loadSongsFromDevice(songRepo)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                loadSongsFromDevice(songRepo)
            }
        }

        setContent {
            val navController = rememberNavController()
            var currentSong by remember { mutableStateOf(null as Song?) }
            var isPlaying by remember { mutableStateOf(false) }

            MusicStreamingTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            listOf(
                                NavRoutes.Library to "Library",
                                NavRoutes.Playlists to "Playlists",
                                NavRoutes.Player to "Now Playing"
                            ).forEach { (route, label) ->
                                NavigationBarItem(
                                    selected = false,
                                    onClick = { navController.navigate(route) },
                                    label = { Text(label) },
                                    icon = {}
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    AppNavHost(
                        navController = navController,
                        songViewModel = songViewModel,
                        playlistViewModel = playlistViewModel,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onPlayPauseClick = { isPlaying = !isPlaying },
                        onSkipNext = { /* TODO */ },
                        onSkipPrevious = { /* TODO */ }
                    )
                }
            }
        }
    }

    private fun loadSongsFromDevice(songRepo: SongRepository) {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val artist = it.getString(artistIndex)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                val song = Song(title = title, artist = artist, filePath = contentUri.toString())
                songs.add(song)
            }
        }

        Log.d("MainActivity", "Loaded: ${songs.size} songs")

        if (songs.isEmpty()) {
            Log.d("MainActivity", "No songs found on device.")
        }

        lifecycleScope.launch {
            songRepo.insertAll(songs)
            Log.d("MainActivity", "Inserted ${songs.size} songs into DB")
        }
    }

}

