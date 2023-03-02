package com.ls.player.manager

import android.content.Context
import com.ls.player.mediaplayer.IMediaPlayer

/**
 * 管理播放器行为
 */
interface IPlayerManager {

    fun getMediaPlayer(): IMediaPlayer

    fun initPlayer(context: Context)

    fun start()

    fun pause()

    fun stop()

    fun release()

    fun releaseSurface()

    fun seekTo(seek: Long)

    fun setMute(mute: Boolean)

    fun setVolume(left: Float, right: Float)

    fun setSpeedPlaying(speed: Float, soundTouch: Boolean)

    fun isPlaying(): Boolean

    fun getNetSpeed(): Long

    fun getDuration(): Long

    fun getCurrentPosition(): Long

    fun getVideoWidth(): Int

    fun getVideoHeight(): Int
}