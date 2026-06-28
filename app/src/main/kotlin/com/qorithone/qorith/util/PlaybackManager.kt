package com.qorithone.qorith.util

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession

class PlaybackManager(private val context: Context) {
    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    fun initialize() {
        if (player == null) {
            player = ExoPlayer.Builder(context).build()
        }
    }

    fun getPlayer(): ExoPlayer? = player

    fun getMediaSession(): MediaSession? = mediaSession

    fun release() {
        mediaSession?.release()
        player?.release()
        player = null
        mediaSession = null
    }
}
