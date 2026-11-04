package com.gilank.musicplayer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.runtime.*
import com.gilank.musicplayer.ui.screens.MainPlayerScreen
import com.gilank.musicplayer.ui.screens.SongListScreen
import com.gilank.musicplayer.ui.theme.GilankMusicPlayerTheme
import com.gilank.musicplayer.ui.viewmodel.PlayerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GilankMusicPlayerTheme {
                // Request permissions for storage and notification
                val permissions = remember {
                    buildList {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            add(Manifest.permission.READ_MEDIA_AUDIO)
                            add(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            add(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }

                val permissionsState = rememberMultiplePermissionsState(permissions = permissions)

                LaunchedEffect(Unit) {
                    if (!permissionsState.allPermissionsGranted) {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }

                val songs by playerViewModel.songs.collectAsState()
                val currentSong by playerViewModel.currentSong.collectAsState()
                val isPlaying by playerViewModel.isPlaying.collectAsState()
                val currentPosition by playerViewModel.currentPosition.collectAsState()
                val duration by playerViewModel.duration.collectAsState()
                val isShuffle by playerViewModel.isShuffle.collectAsState()
                val repeatState by playerViewModel.repeatState.collectAsState()
                val isLoading by playerViewModel.isLoading.collectAsState()

                var isPlayerExpanded by remember { mutableStateOf(false) }

                if (isPlayerExpanded && currentSong != null) {
                    MainPlayerScreen(
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        currentPosition = currentPosition,
                        duration = duration,
                        isShuffle = isShuffle,
                        repeatState = repeatState,
                        onTogglePlayPause = playerViewModel::togglePlayPause,
                        onNext = playerViewModel::playNext,
                        onPrevious = playerViewModel::playPrevious,
                        onSeekTo = playerViewModel::seekTo,
                        onToggleShuffle = playerViewModel::toggleShuffle,
                        onToggleRepeat = playerViewModel::toggleRepeat,
                        onCloseScreen = { isPlayerExpanded = false }
                    )
                } else {
                    SongListScreen(
                        songs = songs,
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        isLoading = isLoading,
                        onSongClick = { song ->
                            playerViewModel.playSong(song)
                        },
                        onRefresh = playerViewModel::refreshAudio,
                        onOpenPlayer = { isPlayerExpanded = true },
                        onTogglePlayPause = playerViewModel::togglePlayPause
                    )
                }
            }
        }
    }
}
