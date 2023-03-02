package com.ls.player.mediaplayer

import android.content.Context
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import java.io.FileDescriptor

interface IMediaPlayer {

    fun setDisplay(surfaceHolder: SurfaceHolder?)

    fun setSurface(surface: Surface?)

    fun setDataSource(context: Context, uri: Uri)

    fun setDataSource(path: String)

    fun setDataSource(fd: FileDescriptor)

    fun setMute(mute: Boolean)

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun seekTo(seek: Long)

    fun preparePlayer()

    fun start()

    fun pause()

    fun stop()

    fun reset()

    fun release()

    fun isPlaying(): Boolean

    fun getCurrentPosition(): Long

    fun getDuration(): Long

    fun getVideoWidth(): Int

    fun getVideoHeight(): Int

    fun setVideoSizeChangedListener(videoSizeChangedListener: OnVideoSizeChangedListener)

    fun setProgressUpdateListener(progressUpdateListener: OnProgressUpdateListener)

    fun setLoadingChangedListener(loadingChangedListener: OnLoadingChangedListener)

    fun setPlayingChangedListener(playingChangedListener: OnPlayingChangedListener)

    fun setPreparedListener(preparedListener: OnPreparedListener)

    fun setCompletionListener(completionListener: OnCompletionListener)

    fun setErrorListener(errorListener: OnErrorListener)

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(width: Int, height: Int)
    }

    interface OnProgressUpdateListener {
        fun onProgressUpdate(position: Long, bufferedPosition: Long)
    }

    interface OnLoadingChangedListener {
        fun onLoadingCHanged(isLoading: Boolean)
    }

    interface OnPlayingChangedListener {
        fun onPlayingChanged(isPlaying: Boolean)
    }

    interface OnPreparedListener {
        fun onPrepared()
    }

    interface OnCompletionListener {
        fun onComplete()
    }

    interface OnErrorListener {
        fun onError(mp: IMediaPlayer, what: Int, extra: Int): Boolean
    }
}