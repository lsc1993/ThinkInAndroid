package com.ls.player.render

import android.view.View

interface IVideoRenderView {

    fun getRenderView(): View

    fun getSizeW(): Int

    fun getSizeH(): Int

    fun onRenderResume()

    fun onRenderPause()

    fun releaseRenderAll()
}