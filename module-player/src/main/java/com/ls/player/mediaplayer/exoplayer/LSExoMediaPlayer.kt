package com.ls.player.mediaplayer.exoplayer

import android.content.Context
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.video.VideoSize
import com.ls.player.mediaplayer.AbstractMediaPlayer
import com.ls.player.mediaplayer.manager.ExoSourceManager
import java.io.FileDescriptor

/**
 * 基于ExoPlayer2实现播放器
 */
class LSExoMediaPlayer(private val context: Context) : AbstractMediaPlayer(), Player.Listener {

    private lateinit var exoPlayer: ExoPlayer

    private val sourceManager: ExoSourceManager = ExoSourceManager.newInstance(context)
    private var mediaSource: MediaSource? = null

    private var videoWidth = 0
    private var videoHeight = 0

    override fun preparePlayer() {
        val renderersFactory = DefaultRenderersFactory(context).apply {
            setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        }
        exoPlayer = ExoPlayer.Builder(context.applicationContext, renderersFactory)
            .setLoadControl(DefaultLoadControl())
            .setTrackSelector(DefaultTrackSelector(context))
            .build()
        mediaSource?.let {
            exoPlayer.setMediaSource(it)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false
        } ?: throw java.lang.Exception("no media source")
    }

    override fun setDisplay(surfaceHolder: SurfaceHolder?) {
        exoPlayer.setVideoSurface(surfaceHolder?.surface)
    }

    override fun setSurface(surface: Surface?) {
        surface?.let {
            if (it.isValid) {
                exoPlayer.setVideoSurface(it)
            } else {
                exoPlayer.setVideoSurface(null)
            }
        } ?: exoPlayer.setVideoSurface(null)
    }

    override fun setDataSource(context: Context, uri: Uri) {
        setDataSource(uri.toString())
    }

    override fun setDataSource(path: String) {
        mediaSource = sourceManager.createMediaSource(path)
    }

    override fun setDataSource(fd: FileDescriptor) {
        throw java.lang.UnsupportedOperationException("not support")
    }

    override fun setMute(mute: Boolean) {
        exoPlayer.volume = 0f
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        exoPlayer.volume = (leftVolume + rightVolume) / 2f
    }

    override fun seekTo(seek: Long) {
        exoPlayer.seekTo(seek)
    }

    override fun start() {
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
    }

    override fun stop() {
        exoPlayer.release()
    }

    override fun reset() {
        
    }

    override fun release() {
        reset()
        videoWidth = 0
        videoHeight = 0
    }

    override fun isPlaying(): Boolean {
        return exoPlayer.isPlaying
    }

    override fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }

    override fun getDuration(): Long {
        return exoPlayer.duration
    }

    override fun getVideoWidth(): Int {
        return videoWidth
    }

    override fun getVideoHeight(): Int {
        return videoHeight
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        videoWidth = videoSize.width
        videoHeight = videoSize.height
        notifyVideoSize(videoSize.width, videoSize.height)
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        notifyLoadingChanged(isLoading)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        notifyPlayingChanged(isPlaying)
    }
}