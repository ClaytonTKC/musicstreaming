package com.example.musicstreaming.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicstreaming.model.Playlist
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.ui.screens.*
import com.example.musicstreaming.viewmodel.PlaylistViewModel
import com.example.musicstreaming.viewmodel.SongViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel,
    currentSong: Song?,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit
) {
    NavHost(navController, startDestination = NavRoutes.Library) {
        composable(NavRoutes.Library) {
            SongLibraryScreen(songViewModel) {
                navController.navigate(NavRoutes.Player)
            }
        }

        composable(NavRoutes.Playlists) {
            PlaylistScreen(playlistViewModel) { playlist ->
                navController.currentBackStackEntry?.savedStateHandle?.set("selectedPlaylist", playlist)
                navController.navigate(NavRoutes.PlaylistDetail)
            }
        }

        composable(NavRoutes.PlaylistDetail) {
            val playlist = navController.previousBackStackEntry
                ?.savedStateHandle?.get<Playlist>("selectedPlaylist")
            if (playlist != null) {
                PlaylistDetailScreen(playlist, playlistViewModel, songViewModel)
            }
        }

        composable(NavRoutes.Player) {
            currentSong?.let {
                PlayerScreen(
                    song = it,
                    isPlaying = isPlaying,
                    onPlayPauseClick = onPlayPauseClick,
                    onSkipNext = onSkipNext,
                    onSkipPrevious = onSkipPrevious
                )
            }
        }
    }
}
