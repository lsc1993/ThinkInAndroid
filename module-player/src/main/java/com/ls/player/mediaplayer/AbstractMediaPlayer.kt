package com.ls.player.mediaplayer

abstract class AbstractMediaPlayer : IMediaPlayer {

    private var videoSizeChangedListener: IMediaPlayer.OnVideoSizeChangedListener? = null

    private var progressUpdateListener: IMediaPlayer.OnProgressUpdateListener? = null

    private var loadingChangedListener: IMediaPlayer.OnLoadingChangedListener? = null

    private var playingChangedListener: IMediaPlayer.OnPlayingChangedListener? = null

    private var preparedListener: IMediaPlayer.OnPreparedListener? = null

    private var completionListener: IMediaPlayer.OnCompletionListener? = null

    private var errorListener: IMediaPlayer.OnErrorListener? = null

    override fun setVideoSizeChangedListener(videoSizeChangedListener: IMediaPlayer.OnVideoSizeChangedListener) {
        this.videoSizeChangedListener = videoSizeChangedListener
    }

    override fun setProgressUpdateListener(progressUpdateListener: IMediaPlayer.OnProgressUpdateListener) {
        this.progressUpdateListener = progressUpdateListener
    }

    override fun setLoadingChangedListener(loadingChangedListener: IMediaPlayer.OnLoadingChangedListener) {
        this.loadingChangedListener = loadingChangedListener
    }

    override fun setPlayingChangedListener(playingChangedListener: IMediaPlayer.OnPlayingChangedListener) {
        this.playingChangedListener = playingChangedListener
    }

    override fun setPreparedListener(preparedListener: IMediaPlayer.OnPreparedListener) {
        this.preparedListener = preparedListener
    }

    override fun setCompletionListener(completionListener: IMediaPlayer.OnCompletionListener) {
        this.completionListener = completionListener
    }

    override fun setErrorListener(errorListener: IMediaPlayer.OnErrorListener) {
        this.errorListener = errorListener
    }

    fun notifyVideoSize(width: Int, height: Int) {
        videoSizeChangedListener?.onVideoSizeChanged(width, height)
    }

    fun notifyProgressUpdate(position: Long, bufferedPosition: Long) {
        progressUpdateListener?.onProgressUpdate(position, bufferedPosition)
    }

    fun notifyLoadingChanged(isLoading: Boolean) {
        loadingChangedListener?.onLoadingCHanged(isLoading)
    }

    fun notifyPlayingChanged(isPlaying: Boolean) {
        playingChangedListener?.onPlayingChanged(isPlaying)
    }

    fun notifyPrepared() {
        preparedListener?.onPrepared()
    }

    fun notifyCompletion() {
        completionListener?.onComplete()
    }

    fun notifyError(what: Int, extra: Int) {
        errorListener?.onError(this, what, extra)
    }
}