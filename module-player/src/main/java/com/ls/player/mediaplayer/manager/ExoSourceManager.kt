package com.ls.player.mediaplayer.manager

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException
import com.google.android.exoplayer2.util.Util
import com.google.common.base.Ascii
import com.ls.player.constants.TYPE_RTMP

/**
 * Exoplayer 资源管理类
 */
class ExoSourceManager private constructor(context: Context) {

    private val appContext: Context

    init {
        appContext = context.applicationContext
    }

    fun createMediaSource(dataSource: String): MediaSource {
        val contentUri = Uri.parse(dataSource)
        val mediaItem = MediaItem.fromUri(contentUri)
        val mediaSource = when (contentUri.scheme) {
            "android.resource" -> {
                val dataSpec = DataSpec(contentUri)
                val rawSourceDataSource = RawResourceDataSource(appContext)
                try {
                    rawSourceDataSource.open(dataSpec)
                } catch (e: RawResourceDataSourceException) {
                    e.printStackTrace()
                }

                val factory = DataSource.Factory {
                    rawSourceDataSource
                }

                ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)
            }
            "assets" -> {
                val dataSpec = DataSpec(contentUri)
                val assetsSourceDataSource = AssetDataSource(appContext)
                try {
                    assetsSourceDataSource.open(dataSpec)
                } catch (e: RawResourceDataSourceException) {
                    e.printStackTrace()
                }

                val factory = DataSource.Factory {
                    assetsSourceDataSource
                }

                ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)
            }
            else -> {
                when (inferContentType(dataSource)) {
                    C.CONTENT_TYPE_SS -> {
                        SsMediaSource.Factory(
                            DefaultSsChunkSource.Factory(getDataSourceFactory(appContext)),
                            DefaultDataSource.Factory(
                                appContext,
                                getHttpDataSourceFactory(appContext)
                            )
                        ).createMediaSource(mediaItem)
                    }
                    C.CONTENT_TYPE_RTSP -> {
                        val factory = RtspMediaSource.Factory().apply {
                            setTimeoutMs(5000)
                            setForceUseRtpTcp(true)
                        }
                        factory.createMediaSource(mediaItem)
                    }
                    C.CONTENT_TYPE_DASH -> {
                        DashMediaSource.Factory(
                            DefaultDashChunkSource.Factory(
                                getDataSourceFactory(
                                    appContext
                                )
                            ),
                            DefaultDataSource.Factory(
                                appContext,
                                getHttpDataSourceFactory(appContext)
                            )
                        ).createMediaSource(mediaItem)
                    }
                    C.CONTENT_TYPE_HLS -> {
                        HlsMediaSource.Factory(getDataSourceFactory(appContext))
                            .setAllowChunklessPreparation(true)
                            .createMediaSource(mediaItem)
                    }
                    else -> {
                        ProgressiveMediaSource.Factory(
                            getDataSourceFactory(appContext),
                            DefaultExtractorsFactory()
                        ).createMediaSource(mediaItem)
                    }
                }
            }
        }

        return mediaSource
    }

    fun getDataSourceFactory(context: Context): DataSource.Factory {
        return DefaultDataSource.Factory(context, getHttpDataSourceFactory(context))
    }

    private fun getHttpDataSourceFactory(context: Context): DataSource.Factory {
        return DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(false)
            .setConnectTimeoutMs(5000)
            .setReadTimeoutMs(5000)
            .setTransferListener(DefaultBandwidthMeter.Builder(context).build())
    }

    /**
     * 通过连接获取资源类型
     */
    private fun inferContentType(dataSource: String): Int {
        val url = Ascii.toLowerCase(dataSource)
        return if (url.startsWith("rtmp:")) {
            TYPE_RTMP
        } else {
            Util.inferContentType(Uri.parse(dataSource))
        }
    }

    companion object {
        fun newInstance(context: Context): ExoSourceManager {
            return ExoSourceManager(context)
        }
    }
}