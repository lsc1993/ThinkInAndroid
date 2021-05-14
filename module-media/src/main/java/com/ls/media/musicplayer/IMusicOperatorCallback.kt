package com.ls.media.musicplayer

interface IMusicOperatorCallback {
  fun updatePlayProgress(progress: Int)
  fun onPlayComplete()
  fun onPlayPause()
}