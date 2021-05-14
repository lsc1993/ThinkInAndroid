package com.ls.media.musicplayer.event

class MusicPlayRestartEvent : MusicPlayEvent() {
  override val eventType = PLAY_EVENT_RESTART
}