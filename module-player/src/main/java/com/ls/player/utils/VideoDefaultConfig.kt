package com.ls.player.utils

object VideoDefaultConfig {

    //默认显示比例
    const val SCREEN_TYPE_DEFAULT = 0

    //16:9
    const val SCREEN_TYPE_16_9 = 1

    //4:3
    const val SCREEN_TYPE_4_3 = 2

    //18:9
    const val SCREEN_TYPE_18_9 = 6

    //全屏裁减显示，为了显示正常 surface_container 和 CoverImageView 建议使用 FrameLayout 作为父布局
    const val SCREEN_TYPE_FULL = 4

    //全屏拉伸显示，使用这个属性时，surface_container 建议使用 FrameLayout
    const val SCREEN_MATCH_FULL = -4

    //自定义比例，需要设置 sScreenScaleRatio
    const val SCREEN_TYPE_CUSTOM = -5

    /**
     * 自定义的显示比例
     */
    private var sScreenScaleRatio = 0f

    /**
     * GLSurfaceView 主要用于OpenGL渲染的
     */
    const val GLSURFACE = 2

    /**
     * SurfaceView，与动画全屏的效果不是很兼容
     */
    const val SUFRACE = 1

    /**
     * TextureView,默认
     */
    const val TEXTURE = 0


    //显示比例类型
    private var TYPE = SCREEN_TYPE_DEFAULT

    //硬解码标志
    private var MEDIA_CODEC_FLAG = false

    //渲染类型
    private var sRenderType = TEXTURE

    //是否使用硬解码优化
    private var sTextureMediaPlay = false


    /**
     * 使能硬解码，播放前设置
     */
    fun enableMediaCodec() {
        MEDIA_CODEC_FLAG = true
    }

    /**
     * 关闭硬解码，播放前设置
     */
    fun disableMediaCodec() {
        MEDIA_CODEC_FLAG = false
    }

    /**
     * 使能硬解码渲染优化
     */
    fun enableMediaCodecTexture() {
        sTextureMediaPlay = true
    }

    /**
     * 关闭硬解码渲染优化
     */
    fun disableMediaCodecTexture() {
        sTextureMediaPlay = false
    }

    /**
     * 是否开启硬解码
     */
    fun isMediaCodec(): Boolean {
        return MEDIA_CODEC_FLAG
    }

    /**
     * 是否开启硬解码渲染优化
     */
    fun isMediaCodecTexture(): Boolean {
        return sTextureMediaPlay
    }

    fun getShowType(): Int {
        return TYPE
    }

    /**
     * 设置显示比例,注意，这是全局生效的
     */
    fun setShowType(type: Int) {
        TYPE = type
    }


    fun getRenderType(): Int {
        return sRenderType
    }

    /**
     * 渲染控件
     *
     * @param renderType
     */
    fun setRenderType(renderType: Int) {
        sRenderType = renderType
    }

    fun getScreenScaleRatio(): Float {
        return sScreenScaleRatio
    }

    /***
     * SCREEN_TYPE_CUSTOM 下自定义显示比例
     * @param screenScaleRatio  高宽比，如 16：9
     */
    fun setScreenScaleRatio(screenScaleRatio: Float) {
        sScreenScaleRatio = screenScaleRatio
    }
}