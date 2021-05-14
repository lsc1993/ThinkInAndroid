package com.ls.media.musicplayer.event

class MusicPlayPauseEVent : MusicPlayEvent() {
  override val eventType = PLAY_EVENT_PAUSE

  var needCallback = true
}