package com.ls.media.musicplayer.event

class MusicPlayInitStartEvent : MusicPlayEvent() {

  override val eventType = PLAY_EVENT_INIT_START

  var musicPath: String? = ""
}