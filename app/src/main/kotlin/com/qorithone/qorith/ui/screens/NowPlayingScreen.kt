package com.qorithone.qorith.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qorithone.qorith.viewmodel.MainViewModel

@Composable
fun NowPlayingScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val currentSong = viewModel.currentPlayingSong.collectAsState().value
    val isPlaying = viewModel.isPlaying.collectAsState().value
    val position = viewModel.currentPosition.collectAsState().value
    val theme = viewModel.currentTheme.collectAsState().value
    val shuffle = viewModel.shuffle.collectAsState().value
    val repeat = viewModel.repeat.collectAsState().value

    val backgroundColor = when (theme) {
        "amoled" -> Color(0xFF000000)
        "dark" -> Color(0xFF0B1220)
        "light" -> Color(0xFFF4F6FA)
        "amoledlight" -> Color(0xFFFFFFFF)
        else -> Color(0xFF000000)
    }

    val textColor = when (theme) {
        "light", "amoledlight" -> Color(0xFF10172A)
        else -> Color(0xFFF2F5FA)
    }

    val accentColor = Color(0xFF39C0F2)

    if (currentSong == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Back", tint = textColor)
            }
            Text(
                "No song playing",
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    val progress = if (currentSong.duration > 0) position.toFloat() / currentSong.duration else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Back", tint = textColor)
            }
            Text(
                "NOW PLAYING",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )
            IconButton(onClick = { /* Info */ }) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = textColor)
            }
        }

        // Album Art
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "🎵",
                fontSize = 80.sp
            )
        }

        // Song Info
        Text(
            currentSong.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            currentSong.folder,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .padding(vertical = 16.dp),
            color = accentColor,
            trackColor = textColor.copy(alpha = 0.1f)
        )

        // Time Display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                formatDuration(position),
                fontSize = 10.sp,
                color = textColor.copy(alpha = 0.6f)
            )
            Text(
                formatDuration(currentSong.duration),
                fontSize = 10.sp,
                color = textColor.copy(alpha = 0.6f)
            )
        }

        // Control Buttons (Shuffle & Repeat)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.toggleShuffle() },
                modifier = Modifier
                    .size(40.dp)
            ) {
                Text(
                    "⤨",
                    fontSize = 18.sp,
                    color = if (shuffle) accentColor else textColor.copy(alpha = 0.6f)
                )
            }
            IconButton(
                onClick = { viewModel.cycleRepeat() },
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    "↻",
                    fontSize = 18.sp,
                    color = if (repeat.name != "OFF") accentColor else textColor.copy(alpha = 0.6f)
                )
            }
        }

        // Main Playback Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous */ }) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = textColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50%))
                    .background(accentColor)
                    .clickable { viewModel.togglePlayPause() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color(0xFF000000),
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(onClick = { /* Next */ }) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = textColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Spacer
        Box(modifier = Modifier.weight(1f))

        // Bottom Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Timer */ }) {
                Text("⏱", fontSize = 20.sp)
            }
            IconButton(onClick = { /* Favorite */ }) {
                Icon(
                    if (currentSong.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (currentSong.isFavorite) Color(0xFFE0517E) else textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { navController.navigate("queue") }) {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Queue",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
