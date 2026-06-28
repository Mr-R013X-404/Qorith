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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
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

@Composable
fun FolderDetailScreen(
    folderName: String,
    viewModel: MainViewModel,
    navController: NavController
) {
    val songs = viewModel.songs.collectAsState().value
    val theme = viewModel.currentTheme.collectAsState().value

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

    val folderSongs = songs.filter { it.folder == folderName }

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
                folderName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                maxLines = 1
            )

            IconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = textColor)
            }
            IconButton(onClick = { /* Sort */ }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort", tint = textColor)
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                icon = Icons.Default.PlayArrow,
                label = "Play",
                onClick = { /* Play all */ },
                modifier = Modifier.weight(1f),
                textColor = textColor
            )
            ActionButton(
                icon = Icons.Default.Shuffle,
                label = "Shuffle",
                onClick = { /* Shuffle play */ },
                modifier = Modifier.weight(1f),
                textColor = textColor
            )
        }

        // Songs List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(folderSongs) { song ->
                SongItem(
                    song = song,
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
fun ActionButton(
    icon: androidx.compose.material.icons.Icons,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF39C0F2).copy(alpha = 0.12f))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF39C0F2),
            modifier = Modifier.size(18.dp)
        )
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF39C0F2),
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}
