# Qorith – Offline-First Music Player

A premium, lightweight, offline music player for Android built with privacy and performance in mind.

## Features

- **Fully Offline**: No internet required, no cloud dependency
- **Privacy-First**: No analytics, no tracking, no user accounts
- **Fast & Lightweight**: Optimized for low-end Android devices
- **Folder-Based Organization**: Browse music by folder structure
- **Powerful Queue System**: Drag-and-drop reordering, queue management
- **Playlists & Favorites**: Create custom playlists and manage favorites
- **Multiple Themes**: AMOLED, Dark, Light, AMOLED Light
- **Material Design 3**: Modern, clean, professional UI
- **Media3 & ExoPlayer**: Robust audio playback

## Screens Implemented

- ✅ **Splash Screen** - Animated intro with logo
- ✅ **Songs Screen** - List all songs with playback controls
- ✅ **Folders Screen** - Browse music by folders
- ✅ **Folder Detail** - View songs in a specific folder
- ✅ **Playlists Screen** - Manage custom playlists
- ✅ **Playlist Detail** - View songs in a playlist
- ✅ **Now Playing Screen** - Full playback controls, seek bar, shuffle/repeat
- ✅ **Queue Screen** - Drag-and-drop queue management
- ✅ **Settings Screen** - Theme selection, playback settings
- ✅ **About Screen** - App info, developer credits

## Architecture

- **MVVM Pattern** with Jetpack Compose
- **Room Database** for local persistence
- **Hilt Dependency Injection**
- **Coroutines** for async operations
- **StateFlow** for reactive UI updates

## Requirements

- Android 8.0+ (minSdk 26)
- Android Studio Hedgehog or newer
- Kotlin 1.9.20+

## Building

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

## Privacy

Qorith is completely offline:
- ✅ No user accounts
- ✅ No cloud sync
- ✅ No analytics
- ✅ No tracking
- ✅ No internet dependency
- ✅ No data collection

## Author

QorithOne

---

**Qorith** – Your music, your way.
