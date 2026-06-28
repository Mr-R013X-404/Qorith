package com.qorithone.qorith.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qorithone.qorith.data.model.RepeatMode
import com.qorithone.qorith.data.model.Song
import com.qorithone.qorith.data.model.Playlist
import com.qorithone.qorith.data.repository.SongRepository
import com.qorithone.qorith.data.repository.PlaylistRepository
import com.qorithone.qorith.data.repository.QueueRepository
import com.qorithone.qorith.ui.screens.PlaylistItemUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val queueRepository: QueueRepository
) : ViewModel() {

    // UI State
    private val _currentTheme = MutableStateFlow("amoled")
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _playlists = MutableStateFlow<List<PlaylistItemUI>>(emptyList())
    val playlists: StateFlow<List<PlaylistItemUI>> = _playlists.asStateFlow()

    private val _currentPlayingSong = MutableStateFlow<Song?>(null)
    val currentPlayingSong: StateFlow<Song?> = _currentPlayingSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _shuffle = MutableStateFlow(false)
    val shuffle: StateFlow<Boolean> = _shuffle.asStateFlow()

    private val _repeat = MutableStateFlow(RepeatMode.OFF)
    val repeat: StateFlow<RepeatMode> = _repeat.asStateFlow()

    private val _queue = MutableStateFlow<List<String>>(emptyList())
    val queue: StateFlow<List<String>> = _queue.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadAllSongs()
        loadPlaylists()
    }

    private fun loadAllSongs() {
        viewModelScope.launch {
            songRepository.getAllSongs().collect { songs ->
                _songs.value = songs
            }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists.map { playlist ->
                    PlaylistItemUI(
                        id = playlist.id,
                        name = playlist.name,
                        songCount = 0, // Will be updated separately
                        isSystem = playlist.isSystem
                    )
                }
            }
        }
    }

    fun setTheme(theme: String) {
        _currentTheme.value = theme
    }

    fun playSong(songId: String) {
        val song = _songs.value.find { it.id == songId }
        if (song != null) {
            _currentPlayingSong.value = song
            _isPlaying.value = true
            _currentPosition.value = 0L
            viewModelScope.launch {
                songRepository.incrementPlayCount(songId)
            }
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun pausePlayback() {
        _isPlaying.value = false
    }

    fun seekTo(position: Long) {
        _currentPosition.value = position
    }

    fun toggleShuffle() {
        _shuffle.value = !_shuffle.value
    }

    fun cycleRepeat() {
        _repeat.value = when (_repeat.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun addToQueue(songId: String) {
        val currentQueue = _queue.value.toMutableList()
        if (!currentQueue.contains(songId)) {
            currentQueue.add(songId)
            _queue.value = currentQueue
        }
    }

    fun playAllSongs(songIds: List<String>) {
        _queue.value = songIds
        if (songIds.isNotEmpty()) {
            playSong(songIds[0])
        }
    }

    fun shufflePlaySongs(songIds: List<String>) {
        val shuffled = songIds.shuffled()
        _queue.value = shuffled
        _shuffle.value = true
        if (shuffled.isNotEmpty()) {
            playSong(shuffled[0])
        }
    }

    fun clearQueue() {
        _queue.value = emptyList()
        viewModelScope.launch {
            queueRepository.clearQueue()
        }
    }
}
