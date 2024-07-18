package com.example.hoangquocanh.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.example.hoangquocanh.MainActivity
import com.example.hoangquocanh.MyApplication
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.local.song.Song
import java.io.IOException

class MusicService : Service() {
    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var song: Song? = null
    private var isPlaying = false
    private var progressListener: ((Int) -> Unit)? = null
    private val NOTIFICATION_ID = 1

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun play(song: Song) {
        stop()
        this.song = song
        Log.d("File path", song.path)
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            try {
                val uri = Uri.parse(song.path)
                mediaPlayer?.setDataSource(this, uri)
                mediaPlayer?.prepareAsync()
                mediaPlayer?.setOnPreparedListener {
                    it.start()
                    this@MusicService.isPlaying = true
                    showNotification()
                    updateProgress()
                }
            } catch (e: IOException) {
                Log.e("MusicService", "Unable to create media player", e)
                mediaPlayer?.release()
                mediaPlayer = null
            }
        } else {
            mediaPlayer?.start()
            isPlaying = true
            showNotification()
            updateProgress()
        }
    }

    private fun updateProgress() {
        val duration = mediaPlayer?.duration ?: 0
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val progress = if (duration > 0) (currentPosition.toFloat() / duration * 100).toInt() else 0
        progressListener?.invoke(progress)
        if (isPlaying) {
            Handler(Looper.getMainLooper()).postDelayed({
                updateProgress()
            }, 1000)
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
        showNotification()
    }
    fun continuePlaying() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
            showNotification()
            updateProgress()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d("MusicService", "Stop")
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun setProgressListener(listener: ((Int) -> Unit)?) {
        progressListener = listener
    }

    private fun showNotification() {
        val playPauseIntent = Intent(this, MusicService::class.java).apply {
            action = if (isPlaying) "PAUSE" else "PLAY"
        }
        val previousIntent = Intent(this, MusicService::class.java).apply {
            action = "PREVIOUS"
        }
        val nextIntent = Intent(this, MusicService::class.java).apply {
            action = "NEXT"
        }
        val exitIntent = Intent(this, MusicService::class.java).apply {
            action = "EXIT"
        }
        val playPausePendingIntent = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val previousPendingIntent = PendingIntent.getService(
            this, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val nextPendingIntent = PendingIntent.getService(
            this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val exitPendingIntent = PendingIntent.getService(
            this, 0, exitIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        val title = if (isPlaying) "Pause" else "Play"
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle(this.song!!.title)
            .setContentText(this.song!!.artist)
            .setSmallIcon(R.drawable.logo_app)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
            .addAction(icon, title, playPausePendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_exit,"Exit",exitPendingIntent)
            .setStyle(MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
            .build()

        try {
            startForeground(NOTIFICATION_ID, notification)
            Log.d("MusicService", "Notification started successfully")
        } catch (e: Exception) {
            Log.e("MusicService", "Failed to start notification", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> play(this.song!!)
            "PAUSE" -> pause()
            "EXIT" -> stop()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
