package com.example.musicstreaming.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicstreaming.model.Playlist
import com.example.musicstreaming.viewmodel.PlaylistViewModel

@Composable
fun PlaylistScreen(
    playlistViewModel: PlaylistViewModel,
    onPlaylistClick: (Playlist) -> Unit
) {
    val playlists by playlistViewModel.playlists.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Playlists", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        playlists.forEach { playlist ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onPlaylistClick(playlist) }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(playlist.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
