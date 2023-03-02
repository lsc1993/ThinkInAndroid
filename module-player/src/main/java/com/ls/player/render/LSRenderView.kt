package com.ls.player.render

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ls.player.constants.RenderType
import com.ls.player.constants.ScreenType
import com.ls.player.render.texture.ILSSurfaceTextureListener
import com.ls.player.render.texture.LSTextureView

class LSRenderView : LifecycleObserver {

    private var renderView: IVideoRenderView? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        renderView?.onRenderResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        renderView?.onRenderPause()
    }

    fun setLayoutParams(layoutParams: ViewGroup.LayoutParams) {
        renderView?.getRenderView()?.layoutParams = layoutParams
    }

    fun addRenderView(
        context: Context,
        container: ViewGroup,
        lsTextureListener: ILSSurfaceTextureListener,
        renderType: RenderType = RenderType.Texture
    ) {
        when (renderType) {
            RenderType.Texture -> {
                LSTextureView.addTextureView(context, container, lsTextureListener)
            }
            RenderType.Surface -> {

            }
            RenderType.GLSurface -> {

            }
        }
    }

    fun fitScreen() {

    }

    companion object {

        var screenType: ScreenType = ScreenType.SCREEN_DEFAULT

        fun addToParent(container: ViewGroup, renderView: View) {
            val params: Int = getTextureParams(screenType)
            if (container is RelativeLayout) {
                val layoutParams = RelativeLayout.LayoutParams(params, params)
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                container.addView(renderView, layoutParams)
            } else if (container is FrameLayout) {
                val layoutParams = FrameLayout.LayoutParams(params, params)
                layoutParams.gravity = Gravity.CENTER
                container.addView(renderView, layoutParams)
            }
        }

        private fun getTextureParams(screenType: ScreenType): Int {
            return if (screenType == ScreenType.SCREEN_DEFAULT) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
        }

        private fun fitScreen(screenType: ScreenType) {

        }
    }
}