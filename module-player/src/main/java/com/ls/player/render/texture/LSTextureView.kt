package com.ls.player.render.texture

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import com.ls.player.render.IVideoRenderView
import com.ls.player.render.LSRenderView
import com.ls.player.utils.VideoDefaultConfig

class LSTextureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureView(context, attrs, defStyleAttr), SurfaceTextureListener, IVideoRenderView {

    private var saveTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null

    var lsTextureListener: ILSSurfaceTextureListener? = null

    init {
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        if (VideoDefaultConfig.isMediaCodec()) {
            saveTexture?.let {
                setSurfaceTexture(saveTexture!!)
            } ?: let {
                saveTexture = surface
                this.mSurface = Surface(saveTexture)
            }
        } else {
            this.mSurface = Surface(surfaceTexture)
        }
        lsTextureListener?.onSurfaceAvailable(this.mSurface)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        lsTextureListener?.onSurfaceSizeChanged(mSurface, width, height)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        lsTextureListener?.onSurfaceDestroyed(mSurface)
        if (VideoDefaultConfig.isMediaCodecTexture()) {
            return saveTexture == null
        }
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        lsTextureListener?.onSurfaceUpdated(mSurface)
    }

    override fun getRenderView(): View {
        return this
    }

    override fun getSizeW(): Int {
        return width
    }

    override fun getSizeH(): Int {
        return height
    }

    override fun onRenderResume() {

    }

    override fun onRenderPause() {

    }

    override fun releaseRenderAll() {

    }

    companion object {

        fun addTextureView(
            context: Context,
            container: ViewGroup,
            lsTextureListener: ILSSurfaceTextureListener
        ) {
            if (container.childCount > 0) {
                container.removeAllViews()
            }
            val textureView = LSTextureView(context).apply {
                this.lsTextureListener = lsTextureListener
            }
            LSRenderView.addToParent(container, textureView)
        }
    }
}