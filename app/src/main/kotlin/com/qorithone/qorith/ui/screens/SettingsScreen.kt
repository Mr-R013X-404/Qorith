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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
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
fun SettingsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val theme = viewModel.currentTheme.collectAsState().value
    val (resumeLast, setResumeLast) = remember { mutableStateOf(true) }
    val (keepAwake, setKeepAwake) = remember { mutableStateOf(false) }
    val (autoSave, setAutoSave) = remember { mutableStateOf(true) }

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
                "Settings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            // APPEARANCE SECTION
            item {
                SectionLabel("Appearance", textColor)
            }

            item {
                ThemeSelectionRow(viewModel, theme, textColor, backgroundColor)
            }

            // PLAYBACK SECTION
            item {
                SectionLabel("Playback", textColor)
            }

            item {
                SettingToggleRow(
                    label = "Resume Last Song",
                    value = resumeLast,
                    onChanged = setResumeLast,
                    textColor = textColor,
                    backgroundColor = backgroundColor
                )
            }

            item {
                SettingToggleRow(
                    label = "Keep Screen Awake",
                    value = keepAwake,
                    onChanged = setKeepAwake,
                    textColor = textColor,
                    backgroundColor = backgroundColor
                )
            }

            // QUEUE SECTION
            item {
                SectionLabel("Queue", textColor)
            }

            item {
                SettingToggleRow(
                    label = "Auto Save Queue",
                    value = autoSave,
                    onChanged = setAutoSave,
                    textColor = textColor,
                    backgroundColor = backgroundColor
                )
            }

            item {
                SettingClickRow(
                    label = "Clear Queue",
                    onClick = { /* Clear */ },
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    isDanger = true
                )
            }

            // INFO SECTION
            item {
                SectionLabel("Info", textColor)
            }

            item {
                SettingClickRow(
                    label = "About Qorith",
                    onClick = { navController.navigate("about") },
                    textColor = textColor,
                    backgroundColor = backgroundColor
                )
            }
        }
    }
}

@Composable
fun SectionLabel(
    label: String,
    textColor: Color
) {
    Text(
        label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = textColor.copy(alpha = 0.6f),
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun SettingToggleRow(
    label: String,
    value: Boolean,
    onChanged: (Boolean) -> Unit,
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = value,
            onCheckedChange = onChanged
        )
    }
}

@Composable
fun SettingClickRow(
    label: String,
    onClick: () -> Unit,
    textColor: Color,
    backgroundColor: Color,
    isDanger: Boolean = false
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
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = if (isDanger) Color(0xFFE0517E) else textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ThemeSelectionRow(
    viewModel: MainViewModel,
    currentTheme: String,
    textColor: Color,
    backgroundColor: Color
) {
    val themes = listOf(
        "amoled" to "AMOLED",
        "dark" to "Dark",
        "light" to "Light",
        "amoledlight" to "AMOLED Light"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        themes.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { (themeKey, themeName) ->
                    ThemeCard(
                        name = themeName,
                        isSelected = currentTheme == themeKey,
                        onClick = { viewModel.setTheme(themeKey) },
                        modifier = Modifier.weight(1f),
                        textColor = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF39C0F2).copy(alpha = 0.2f) else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            name,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color(0xFF39C0F2) else textColor
        )
    }
}
