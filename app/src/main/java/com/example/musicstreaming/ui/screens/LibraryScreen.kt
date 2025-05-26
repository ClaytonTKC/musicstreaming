package com.example.musicstreaming.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicstreaming.viewmodel.SongViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import com.example.musicstreaming.ui.components.SongCard


@Composable
fun LibraryScreen(songViewModel: SongViewModel) {
    val songs by songViewModel.songs.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("All Songs", style = MaterialTheme.typography.headlineSmall)

        if (songs.isEmpty()) {
            Text("No songs found.")
        } else {
            LazyColumn {
                items(songs, key = { it.id }) { song ->
                    SongCard(song = song) {
                        // TODO: handle click (e.g., navigate to player or play the song)
                    }
                }

            }
        }
    }
}




