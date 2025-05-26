package com.example.musicstreaming.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.viewmodel.SongViewModel

@Composable
fun SongLibraryScreen(
    songViewModel: SongViewModel,
    onSongClick: (Song) -> Unit
) {
    val songs by songViewModel.songs.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("All Songs", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        songs.forEach { song ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSongClick(song) }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(song.title, style = MaterialTheme.typography.bodyLarge)
                    Text(song.artist, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
