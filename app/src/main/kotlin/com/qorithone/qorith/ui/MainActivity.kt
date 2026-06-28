package com.qorithone.qorith.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.qorithone.qorith.ui.screens.SplashScreen
import com.qorithone.qorith.ui.screens.SongsScreen
import com.qorithone.qorith.ui.screens.FoldersScreen
import com.qorithone.qorith.ui.screens.PlaylistsScreen
import com.qorithone.qorith.ui.screens.NowPlayingScreen
import com.qorithone.qorith.ui.screens.SettingsScreen
import com.qorithone.qorith.ui.screens.AboutScreen
import com.qorithone.qorith.ui.screens.QueueScreen
import com.qorithone.qorith.ui.screens.FolderDetailScreen
import com.qorithone.qorith.ui.theme.QorithTheme
import com.qorithone.qorith.viewmodel.MainViewModel
import com.qorithone.qorith.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        permissionManager = PermissionManager(this)

        // Request permissions
        requestPermissions()

        setContent {
            QorithTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QorithApp(viewModel)
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true ||
                permissions[Manifest.permission.READ_MEDIA_AUDIO] == true
            ) {
                viewModel.scanMusicLibrary(this)
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        )
    }
}

@Composable
fun QorithApp(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("songs") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable("songs") {
            SongsScreen(viewModel = viewModel, navController = navController)
        }

        composable("folders") {
            FoldersScreen(viewModel = viewModel, navController = navController)
        }

        composable(
            "folderDetail/{folderName}",
            arguments = listOf(navArgument("folderName") { type = NavType.StringType })
        ) { backStackEntry ->
            val folderName = backStackEntry.arguments?.getString("folderName") ?: return@composable
            FolderDetailScreen(
                folderName = folderName,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("playlists") {
            PlaylistsScreen(viewModel = viewModel, navController = navController)
        }

        composable("nowplaying") {
            NowPlayingScreen(viewModel = viewModel, navController = navController)
        }

        composable("queue") {
            QueueScreen(viewModel = viewModel, navController = navController)
        }

        composable("settings") {
            SettingsScreen(viewModel = viewModel, navController = navController)
        }

        composable("about") {
            AboutScreen(navController = navController, viewModel = viewModel)
        }
    }
}
