package com.ls.media.musicplayer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ls.media.musicplayer.event.MusicPlayEvent
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_INIT_START
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_PAUSE
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_RESET
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_RESTART
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_STOP
import com.ls.media.musicplayer.event.MusicPlayEvent.Companion.PLAY_EVENT_UPDATE_PROGRESS
import com.ls.media.musicplayer.event.MusicPlayInitStartEvent
import com.ls.media.musicplayer.event.MusicPlayPauseEVent
import com.ls.media.musicplayer.event.MusicPlayResetEvent
import com.ls.media.musicplayer.event.MusicPlayRestartEvent
import com.ls.media.musicplayer.event.MusicPlayStopEvent

/**
 * MediaPlayer运行管理类
 * 通过HandlerThread统一MediaPlayer操作管理
 * 以消息循环模型处理MediaPlayer事件
 * @see MusicPlayInitStartEvent 第一次播放
 * @see MusicPlayRestartEvent 重新播放
 * @see MusicPlayPauseEVent  暂停播放
 * @see MusicPlayStopEvent  播放器停止（再次播放需要重新prepare）
 * @see MusicPlayResetEvent  重置播放器到上一个状态
 *
 * @link  https://developer.android.com/images/mediaplayer_state_diagram.gif?hl=zh-cn
 */
class MusicPlayerWorker : HandlerThread, LifecycleObserver {

  private constructor(name: String): super(name) {
    init()
  }

  private constructor(name: String, pro: Int): super(name, pro) {
    init()
  }

  private var isInit = false
  private var lastMsgWhat = -1
  private val curMediaSource = MediaSource("", 0, PLAY_EVENT_INIT_START)

  private var isPlaying = false

  /**
   * 记录播放器错误次数，为了防止循环错误
   * @see resetMediaPlayer 在放生错误时会重置播放器上一个状态
   */
  private var errorCount = 0

  private lateinit var handler: MusicPlayHandler
  private val progressHandler: Handler = ProgressHandler(Looper.getMainLooper())

  private lateinit var mediaPlayer: MediaPlayer

  var musicOperatorCallback: IMusicOperatorCallback? = null

  private fun init() {
    if (!isInit) {
      isInit = true
      initMediaPlayer()
      start()
      handler = MusicPlayHandler(looper)
    }
  }

  private fun initMediaPlayer() {
    mediaPlayer = MediaPlayer()
    mediaPlayer.setAudioAttributes(
      AudioAttributes.Builder().setLegacyStreamType(
        AudioManager.STREAM_MUSIC
      ).build()
    )
    mediaPlayer.isLooping = false
    mediaPlayer.setOnCompletionListener(completeListener)
    mediaPlayer.setOnErrorListener(errorListener)
  }

  fun startPlay(path: String?, preTask: Runnable = Runnable {}, afterTask: Runnable = Runnable {}) {
    removeLastMsg(PLAY_EVENT_INIT_START)
    path?.let {
      curMediaSource.path = path
      curMediaSource.state = PLAY_EVENT_INIT_START
      curMediaSource.progress = 0
    }
    val event = MusicPlayInitStartEvent()
    event.musicPath = path
    event.prePlayTask = preTask
    event.afterPlayTask = afterTask

    val msg = Message.obtain(handler, PLAY_EVENT_INIT_START, event)
    handler.sendMessage(msg)
  }

  fun restartPlay(preTask: Runnable = Runnable {}, afterTask: Runnable = Runnable {}) {
    removeLastMsg(PLAY_EVENT_RESTART)
    val event = MusicPlayRestartEvent()
    event.prePlayTask = preTask
    event.afterPlayTask = afterTask

    val msg = Message.obtain(handler, PLAY_EVENT_RESTART, event)
    handler.sendMessage(msg)
  }

  fun pausePlay(playEvent: MusicPlayPauseEVent = MusicPlayPauseEVent(), preTask: Runnable = Runnable {}, afterTask: Runnable = Runnable {}) {
    removeLastMsg(PLAY_EVENT_PAUSE)
    playEvent.prePlayTask = preTask
    playEvent.afterPlayTask = afterTask

    val msg = Message.obtain(handler, PLAY_EVENT_PAUSE, playEvent)
    handler.sendMessage(msg)
  }

  fun seek(progress: Int) {
    try {
      mediaPlayer.seekTo(progress)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun stopPlay(preTask: Runnable = Runnable {}, afterTask: Runnable = Runnable {}) {
    removeLastMsg(PLAY_EVENT_STOP)
    val event = MusicPlayStopEvent()
    event.prePlayTask = preTask
    event.afterPlayTask = afterTask

    val msg = Message.obtain(handler, PLAY_EVENT_STOP, event)
    handler.sendMessage(msg)
  }

  private fun resetMediaPlayer() {
    removeLastMsg(PLAY_EVENT_RESET)
    val event = MusicPlayResetEvent()

    val msg = Message.obtain(handler, PLAY_EVENT_RESET, event)
    handler.sendMessage(msg)
  }

  fun release() {
    isPlaying = false
    removeUpdateProgressEvent()
    releasePlayer()
    quit()
    instance = null
  }

  private fun releasePlayer() {
    try {
      mediaPlayer.stop()
      mediaPlayer.release()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  /**
   * 移除掉上一个在队列中的消息
   * 保证消息队列中同一时间只存在一个消息
   */
  private fun removeLastMsg(curEvent: Int) {
    if (lastMsgWhat != PLAY_EVENT_RESET && lastMsgWhat != -1) {
      handler.removeMessages(lastMsgWhat)
    }
    lastMsgWhat = curEvent
  }

  fun isPlaying(): Boolean {
    val result: Boolean
    try {
      result = mediaPlayer.isPlaying
    } catch (e: IllegalStateException) {
      return false
    }
    return result
  }

  fun removeUpdateProgressEvent() {
    progressHandler.removeMessages(PLAY_EVENT_UPDATE_PROGRESS)
  }

  private fun getPlayerCurrPosition(): Int {
    return try {
      mediaPlayer.currentPosition
    } catch (e: java.lang.IllegalStateException) {
      return 0
    }
  }

  private val errorListener: MediaPlayer.OnErrorListener = MediaPlayer.OnErrorListener { _, _, _ ->
    val result = if (errorCount < MAX_RESET_TIME) {
      errorCount ++
      resetMediaPlayer()
      true
    } else {
      // 返回false将产生一个complete回调
      false
    }
    result
  }


  private val completeListener: MediaPlayer.OnCompletionListener = MediaPlayer.OnCompletionListener {
    removeUpdateProgressEvent()
    musicOperatorCallback?.onPlayComplete()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    release()
  }

  private inner class MusicPlayHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
      if (msg.obj is MusicPlayEvent) {
        val playEvent = msg.obj as MusicPlayEvent
        playEvent.prePlayTask.run()

        if (msg.what != PLAY_EVENT_RESET) {
          errorCount = 0
        }

        when (msg.what) {
          PLAY_EVENT_INIT_START -> {
            if (playEvent is MusicPlayInitStartEvent) {
              playEvent.musicPath?.let {
                try {
                  curMediaSource.run {
                    this.path = it
                    this.state = PLAY_EVENT_INIT_START
                    this.progress = 0
                  }
                  isPlaying = true
                  progressHandler.sendEmptyMessageDelayed(PLAY_EVENT_UPDATE_PROGRESS, UPDATE_PROGRESS_DELAY_TIME)
                  mediaPlayer.stop()
                  mediaPlayer.reset()  // Idle 状态
                  mediaPlayer.setDataSource(it) // Initialized 状态
                  mediaPlayer.prepare() // prepare 状态
                  mediaPlayer.start()  // start 状态
                } catch (e: Exception) {
                  e.printStackTrace()
                }
              }
            }
          }

          PLAY_EVENT_RESTART -> {
            if (playEvent is MusicPlayRestartEvent) {
              try {
                curMediaSource.run {
                  this.state = PLAY_EVENT_RESTART
                  this.progress = getPlayerCurrPosition()
                }
                isPlaying = true
                val progress = getPlayerCurrPosition()
                mediaPlayer.seekTo(progress)
                mediaPlayer.start()
                progressHandler.sendEmptyMessageDelayed(PLAY_EVENT_UPDATE_PROGRESS, UPDATE_PROGRESS_DELAY_TIME)
              } catch (e: Exception) {
                e.printStackTrace()
              }
            }
          }

          PLAY_EVENT_PAUSE -> {
            if (playEvent is MusicPlayPauseEVent) {
              try {
                curMediaSource.run {
                  this.state = PLAY_EVENT_PAUSE
                  this.progress = getPlayerCurrPosition()
                }
                isPlaying = false
                removeUpdateProgressEvent()
                mediaPlayer.pause()
                if (playEvent.needCallback) {
                  musicOperatorCallback?.onPlayPause()
                }
              } catch (e: Exception) {
                isPlaying = false
                e.printStackTrace()
              }
            }
          }

          PLAY_EVENT_STOP -> {
            if (playEvent is MusicPlayStopEvent) {
              try {
                curMediaSource.run {
                  this.state = PLAY_EVENT_STOP
                  this.progress = 0
                }
                isPlaying = false
                removeUpdateProgressEvent()
                mediaPlayer.stop()
              } catch (e: Exception) {
                e.printStackTrace()
              }
            }
          }

          PLAY_EVENT_RESET -> {
            if (playEvent is MusicPlayResetEvent) {
              try {
                isPlaying = false
                mediaPlayer.reset()
                if (!TextUtils.isEmpty(curMediaSource.path)) {
                  mediaPlayer.setDataSource(curMediaSource.path)
                  mediaPlayer.prepare()
                  when (curMediaSource.state) {
                    PLAY_EVENT_INIT_START -> {
                      mediaPlayer.start()
                      isPlaying = true
                      progressHandler.sendEmptyMessageDelayed(PLAY_EVENT_UPDATE_PROGRESS, UPDATE_PROGRESS_DELAY_TIME)
                    }
                    PLAY_EVENT_PAUSE -> {
                      mediaPlayer.start()
                      mediaPlayer.pause()
                      isPlaying = false
                      removeUpdateProgressEvent()
                    }
                    PLAY_EVENT_RESTART -> {
                      mediaPlayer.seekTo(curMediaSource.progress)
                      mediaPlayer.start()
                      isPlaying = true
                      progressHandler.sendEmptyMessageDelayed(PLAY_EVENT_UPDATE_PROGRESS, UPDATE_PROGRESS_DELAY_TIME)
                    }
                  }
                }
              } catch (e: Exception) {
                errorCount ++
                isPlaying = false
                e.printStackTrace()
              }
            }
          }
        }
        playEvent.afterPlayTask.run()
      }
    }
  }

  private inner class ProgressHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
      musicOperatorCallback?.updatePlayProgress(getPlayerCurrPosition())
      progressHandler.sendEmptyMessageDelayed(PLAY_EVENT_UPDATE_PROGRESS, UPDATE_PROGRESS_DELAY_TIME)
    }
  }

  private class MediaSource(var path: String, var progress: Int, var state: Int)

  companion object {
    private const val UPDATE_PROGRESS_DELAY_TIME = 300L
    private const val MAX_RESET_TIME = 3

    private var instance: MusicPlayerWorker? = null
    get() {
      if (field == null) {
        field = MusicPlayerWorker("MusicPlayer")
      }
      return field
    }

    fun getPlayer(): MusicPlayerWorker {
      return instance!!
    }
  }
}