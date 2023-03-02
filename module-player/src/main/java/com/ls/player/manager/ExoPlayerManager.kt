package com.ls.player.manager

import android.content.Context
import com.ls.player.mediaplayer.IMediaPlayer
import com.ls.player.mediaplayer.exoplayer.LSExoMediaPlayer

class ExoPlayerManager : IPlayerManager {

    private lateinit var context: Context

    private lateinit var player: IMediaPlayer

    override fun getMediaPlayer(): IMediaPlayer {
        return player
    }

    override fun initPlayer(context: Context) {
        this.context = context.applicationContext
        player = LSExoMediaPlayer(this.context)
    }

    override fun start() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun release() {
        player.release()
    }

    override fun releaseSurface() {

    }

    override fun seekTo(seek: Long) {
        player.seekTo(seek)
    }

    override fun setMute(mute: Boolean) {
        player.setMute(mute)
    }

    override fun setVolume(left: Float, right: Float) {
        player.setVolume(left, right)
    }

    override fun setSpeedPlaying(speed: Float, soundTouch: Boolean) {
        //player.set
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying()
    }


    override fun getNetSpeed(): Long {
        return 1
    }

    override fun getDuration(): Long {
        return player.getDuration()
    }

    override fun getCurrentPosition(): Long {
        return player.getCurrentPosition()
    }

    override fun getVideoWidth(): Int {
        return player.getVideoWidth()
    }

    override fun getVideoHeight(): Int {
        return player.getVideoHeight()
    }
}