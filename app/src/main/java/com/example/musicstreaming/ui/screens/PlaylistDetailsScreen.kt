package com.example.musicstreaming.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicstreaming.model.Playlist
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.viewmodel.PlaylistViewModel
import com.example.musicstreaming.viewmodel.SongViewModel

@Composable
fun PlaylistDetailScreen(
    playlist: Playlist,
    playlistViewModel: PlaylistViewModel,
    songViewModel: SongViewModel
) {
    val allSongs by songViewModel.songs.collectAsState()
    var currentPlaylist by remember { mutableStateOf(playlist) }

    Column(Modifier.padding(16.dp)) {
        Text("Playlist: ${playlist.name}", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        allSongs.forEach { song ->
            val isInPlaylist = song.id in currentPlaylist.songIds

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(song.title)
                    Text(song.artist, style = MaterialTheme.typography.bodySmall)
                }

                Button(onClick = {
                    currentPlaylist = if (isInPlaylist) {
                        currentPlaylist.copy(songIds = currentPlaylist.songIds - song.id)
                    } else {
                        currentPlaylist.copy(songIds = currentPlaylist.songIds + song.id)
                    }
                    playlistViewModel.updatePlaylist(currentPlaylist)
                }) {
                    Text(if (isInPlaylist) "Remove" else "Add")
                }
            }
        }
    }
}
