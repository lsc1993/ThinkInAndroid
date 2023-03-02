package com.ls.player.render.texture

import android.view.Surface

interface ILSSurfaceTextureListener {

    fun onSurfaceAvailable(surface: Surface?)

    fun onSurfaceSizeChanged(surface: Surface?, width: Int, height: Int)

    fun onSurfaceDestroyed(surface: Surface?): Boolean

    fun onSurfaceUpdated(surface: Surface?)
}