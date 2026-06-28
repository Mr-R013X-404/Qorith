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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import com.qorithone.qorith.viewmodel.MainViewModel

data class PlaylistItemUI(
    val id: String,
    val name: String,
    val songCount: Int,
    val isSystem: Boolean = false
)

@Composable
fun PlaylistsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val theme = viewModel.currentTheme.collectAsState().value
    val playlists = viewModel.playlists.collectAsState().value

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
            Text(
                "Playlists",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Box(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = textColor)
            }
            IconButton(onClick = { /* Sort */ }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort", tint = textColor)
            }
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = textColor)
            }
        }

        // Playlists List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistItemRow(
                    playlist = playlist,
                    viewModel = viewModel,
                    navController = navController,
                    textColor = textColor,
                    backgroundColor = backgroundColor
                )
            }
        }
    }
}

@Composable
fun PlaylistItemRow(
    playlist: PlaylistItemUI,
    viewModel: MainViewModel,
    navController: NavController,
    textColor: Color,
    backgroundColor: Color
) {
    val itemBackground = if (backgroundColor == Color(0xFFFFFFFF) || backgroundColor == Color(0xFFF4F6FA)) {
        Color(0xFFF0F0F0)
    } else {
        Color(0xFF161F35)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(itemBackground)
            .clickable { navController.navigate("playlistDetail/${playlist.id}") }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (playlist.isSystem) Color(0xFFE0517E) else Color(0xFF1976E0)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (playlist.isSystem) Icons.Default.FavoriteBorder else Icons.Default.List,
                contentDescription = "Playlist",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                playlist.name,
                fontSize = 13.5f.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                maxLines = 1
            )
            Text(
                "${playlist.songCount} songs",
                fontSize = 11.5f.sp,
                color = textColor.copy(alpha = 0.6f),
                maxLines = 1
            )
        }
    }
}
