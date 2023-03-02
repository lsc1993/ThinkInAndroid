package com.ls.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import com.ls.player.constants.RenderType
import com.ls.player.constants.urlH
import com.ls.player.databinding.ActivityPlayVideoBinding
import com.ls.player.factory.PlayerManagerFactory
import com.ls.player.manager.ExoPlayerManager
import com.ls.player.manager.IPlayerManager
import com.ls.player.mediaplayer.IMediaPlayer
import com.ls.player.render.IVideoRenderView
import com.ls.player.render.LSRenderView
import com.ls.player.render.texture.ILSSurfaceTextureListener

class PlayVideoActivity : AppCompatActivity() {

    private lateinit var root: ActivityPlayVideoBinding

    private lateinit var renderView: LSRenderView

    private lateinit var playerManager: IPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ActivityPlayVideoBinding.inflate(layoutInflater)
        setContentView(root.root)

        initView()
    }

    private fun initView() {
        playerManager = PlayerManagerFactory.createPlayerManager(ExoPlayerManager::class.java)
        playerManager.initPlayer(this)
        playerManager.getMediaPlayer().setPreparedListener(object : IMediaPlayer.OnPreparedListener {
            override fun onPrepared() {

            }
        })
        playerManager.getMediaPlayer().setVideoSizeChangedListener(object : IMediaPlayer.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(width: Int, height: Int) {
                renderView.fitScreen()
            }
        })
        playerManager.getMediaPlayer().setDataSource(urlH)
        playerManager.getMediaPlayer().preparePlayer()
        playerManager.start()

        renderView = LSRenderView()
        lifecycle.addObserver(renderView)
        renderView.addRenderView(this, root.flRenderContainer, object : ILSSurfaceTextureListener {
            override fun onSurfaceAvailable(surface: Surface?) {
                playerManager.getMediaPlayer().setSurface(surface)
            }

            override fun onSurfaceSizeChanged(surface: Surface?, width: Int, height: Int) {
            }

            override fun onSurfaceDestroyed(surface: Surface?): Boolean {
               return true
            }

            override fun onSurfaceUpdated(surface: Surface?) {

            }

        }, RenderType.Texture)
    }

    override fun onResume() {
        super.onResume()
        if (!playerManager.isPlaying()) {
            playerManager.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerManager.isPlaying()) {
            playerManager.pause()
        }
    }
}