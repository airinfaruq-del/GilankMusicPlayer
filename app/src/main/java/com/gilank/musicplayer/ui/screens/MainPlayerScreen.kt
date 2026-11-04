package com.gilank.musicplayer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gilank.musicplayer.model.Song
import com.gilank.musicplayer.ui.theme.*
import com.gilank.musicplayer.ui.viewmodel.RepeatState

@Composable
fun MainPlayerScreen(
    currentSong: Song?,
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    isShuffle: Boolean,
    repeatState: RepeatState,
    onTogglePlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onCloseScreen: () -> Unit
) {
    // Vinyl rotation animation while playing
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BlackObsidian,
                        Color(0xFF14120E),
                        BlackObsidian
                    )
                )
            )
            .padding(horizontal = 24.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Back & Header Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onCloseScreen) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Tutup",
                    tint = GoldPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SEDANG MEMUTAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "GILANK MUSIC PLAYER",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }
            IconButton(onClick = { /* More options */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = GoldPrimary
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // Center Album Art / Vinyl Disc
        Box(
            modifier = Modifier
                .size(280.dp)
                .shadow(elevation = 28.dp, shape = CircleShape, spotColor = GoldPrimary)
                .clip(CircleShape)
                .background(BlackCard)
                .border(4.dp, Brush.radialGradient(listOf(GoldLight, GoldPrimary, GoldDark)), CircleShape)
                .rotate(if (isPlaying) rotation else 0f),
            contentAlignment = Alignment.Center
        ) {
            // Album art from media store or fallback
            AsyncImage(
                model = currentSong?.albumArtUri,
                contentDescription = currentSong?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
            )

            // Vinyl center ring with monogram
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BlackObsidian)
                    .border(2.dp, GoldPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldLight
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.15f))

        // Song Title & Artist
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentSong?.title ?: "Pilih Lagu untuk Diputar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = currentSong?.artist ?: "Gilank Music Player",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GoldPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Bar & Duration
        val progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
        var isDragging by remember { mutableStateOf(false) }
        var dragProgress by remember { mutableFloatStateOf(0f) }

        Slider(
            value = if (isDragging) dragProgress else progress.coerceIn(0f, 1f),
            onValueChange = {
                isDragging = true
                dragProgress = it
            },
            onValueChangeFinished = {
                isDragging = false
                val newPosition = (dragProgress * duration).toLong()
                onSeekTo(newPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = GoldLight,
                activeTrackColor = GoldPrimary,
                inactiveTrackColor = BlackBorder
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(if (isDragging) (dragProgress * duration).toLong() else currentPosition),
                fontSize = 12.sp,
                color = TextMuted
            )
            Text(
                text = formatTime(duration),
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Controls: Shuffle, Prev, Play/Pause, Next, Repeat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shuffle
            IconButton(onClick = onToggleShuffle) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Acak",
                    tint = if (isShuffle) GoldLight else TextSubtle,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Previous
            IconButton(onClick = onPrevious) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Sebelumnya",
                    tint = GoldPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Glowing Play/Pause FAB
            FloatingActionButton(
                onClick = onTogglePlayPause,
                containerColor = GoldPrimary,
                contentColor = BlackObsidian,
                shape = CircleShape,
                modifier = Modifier
                    .size(68.dp)
                    .shadow(16.dp, CircleShape, spotColor = GoldLight)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Jeda" else "Putar",
                    modifier = Modifier.size(36.dp)
                )
            }

            // Next
            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Berikutnya",
                    tint = GoldPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Repeat Mode
            IconButton(onClick = onToggleRepeat) {
                Icon(
                    imageVector = if (repeatState == RepeatState.ONE) Icons.Default.RepeatOne else Icons.Default.Repeat,
                    contentDescription = "Ulangi",
                    tint = if (repeatState != RepeatState.OFF) GoldLight else TextSubtle,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
