package com.ls.media.musicplayer.event

abstract class MusicPlayEvent {
  open val eventType = PLAY_EVENT_INIT_START

  // 播放前执行的任务
  var prePlayTask: Runnable = Runnable {  }

  // 播放后执行的任务
  var afterPlayTask: Runnable = Runnable {  }

  companion object {
    const val PLAY_EVENT_INIT_START = 0
    const val PLAY_EVENT_RESTART = 1
    const val PLAY_EVENT_PAUSE = 2
    const val PLAY_EVENT_STOP = 3
    const val PLAY_EVENT_UPDATE_PROGRESS = 4
    const val PLAY_EVENT_RESET = 5
  }
}