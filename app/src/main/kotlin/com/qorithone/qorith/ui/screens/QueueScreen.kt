package com.qorithone.qorith.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qorithone.qorith.data.model.Song
import com.qorithone.qorith.viewmodel.MainViewModel

@Composable
fun QueueScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val queue = viewModel.queue.collectAsState().value
    val songs = viewModel.songs.collectAsState().value
    val currentPlayingSong = viewModel.currentPlayingSong.collectAsState().value
    val theme = viewModel.currentTheme.collectAsState().value
    val selectedSongs = viewModel.selectedSongs.collectAsState().value
    val isSelectMode = viewModel.isSelectMode.collectAsState().value

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

    val queueSongs = queue.mapNotNull { songId ->
        songs.find { it.id == songId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textColor)
            }
            Text(
                "Queue (${queueSongs.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp)
            )

            Box(modifier = Modifier.weight(1f))

            if (isSelectMode) {
                Text(
                    "${selectedSongs.size} selected",
                    fontSize = 12.sp,
                    color = Color(0xFF39C0F2),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            IconButton(onClick = { viewModel.toggleSelectMode() }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = textColor)
            }
        }

        if (queueSongs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Queue is empty",
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                itemsIndexed(queueSongs) { index, song ->
                    val isSelected = selectedSongs.contains(song.id)
                    QueueItemRow(
                        song = song,
                        index = index,
                        isPlaying = song.id == currentPlayingSong?.id,
                        isNextUp = index == (queueSongs.indexOf(currentPlayingSong) + 1),
                        isSelected = isSelected,
                        isSelectMode = isSelectMode,
                        viewModel = viewModel,
                        textColor = textColor,
                        backgroundColor = backgroundColor
                    )
                }
            }
        }
    }
}

@Composable
fun QueueItemRow(
    song: Song,
    index: Int,
    isPlaying: Boolean,
    isNextUp: Boolean,
    isSelected: Boolean,
    isSelectMode: Boolean,
    viewModel: MainViewModel,
    textColor: Color,
    backgroundColor: Color
) {
    val itemBackground = if (backgroundColor == Color(0xFFFFFFFF) || backgroundColor == Color(0xFFF4F6FA)) {
        Color(0xFFF0F0F0)
    } else if (isPlaying) {
        Color(0xFF39C0F2).copy(alpha = 0.15f)
    } else if (isSelected) {
        Color(0xFF39C0F2).copy(alpha = 0.1f)
    } else {
        Color(0xFF161F35)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(itemBackground)
            .clickable {
                if (isSelectMode) {
                    viewModel.toggleSongSelection(song.id)
                } else {
                    viewModel.playSong(song.id)
                }
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelectMode) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (isSelected) Color(0xFF39C0F2) else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Icon(
                Icons.Default.DragIndicator,
                contentDescription = "Drag",
                tint = textColor.copy(alpha = 0.4f),
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = if (isSelectMode) 8.dp else 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    song.title,
                    fontSize = 13.5f.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPlaying) Color(0xFF39C0F2) else textColor,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                if (isNextUp) {
                    Text(
                        "• Next",
                        fontSize = 8.sp,
                        color = textColor.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Text(
                song.folder,
                fontSize = 11.5f.sp,
                color = textColor.copy(alpha = 0.6f),
                maxLines = 1,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (isPlaying) {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(2.dp, 12.dp)
                            .background(Color(0xFF39C0F2))
                    )
                }
            }
        }

        IconButton(onClick = { /* Menu */ }, modifier = Modifier.size(32.dp)) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = textColor.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
