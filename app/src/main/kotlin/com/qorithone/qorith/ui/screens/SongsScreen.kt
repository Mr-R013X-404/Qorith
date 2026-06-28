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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun SongsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val songs = viewModel.songs.collectAsState().value
    val theme = viewModel.currentTheme.collectAsState().value
    val searchQuery = viewModel.searchQuery.collectAsState().value
    val (showSearch, setShowSearch) = remember { mutableStateOf(false) }

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

    val filteredSongs = if (searchQuery.isNotEmpty()) {
        songs.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.folder.contains(searchQuery, ignoreCase = true)
        }
    } else {
        songs
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
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF39C0F2)),
                contentAlignment = Alignment.Center
            ) {
                Text("Q", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Text(
                "Qorith",
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Box(modifier = Modifier.weight(1f))

            IconButton(onClick = { setShowSearch(!showSearch) }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = textColor)
            }
            IconButton(onClick = { /* Sort */ }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort", tint = textColor)
            }
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = textColor)
            }
        }

        // Search Bar
        if (showSearch) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(textColor.copy(alpha = 0.1f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.search(it) },
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                        color = textColor,
                        fontSize = 14.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                "Search songs, folders...",
                                color = textColor.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )
                IconButton(onClick = { setShowSearch(false); viewModel.search("") }, modifier = Modifier.size(24.dp)) {
                    Text("✕", color = textColor, fontSize = 16.sp)
                }
            }
        }

        // Songs List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(filteredSongs) { song ->
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
