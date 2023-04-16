package com.hs.media.services

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hs.media.R
import com.hs.media.view.AudioPlayerActivity
import java.io.File

class AudioPlayerService : Service() {
    // Media player
    private lateinit var mediaPlayer: MediaPlayer

    // Notification
    private lateinit var notification: Notification
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()

        // Initialize media player
        mediaPlayer = MediaPlayer()

        // Set up notification
        createNotificationChannel()
        notification = buildNotification()

        // Start service in foreground with the notification
        startForeground(notificationId, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Retrieve the file URI or file path from the intent
        val fileUri = intent?.getParcelableExtra<Uri>("File")
        val filePath = intent?.getStringExtra("File")

        // Set data source for media player
        mediaPlayer.setDataSource(this, fileUri ?: Uri.fromFile(File(filePath)))
        mediaPlayer.prepare()
        mediaPlayer.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        stopForeground(true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        // Build notification with controls
        val notificationIntent = Intent(this, AudioPlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playPauseAction = NotificationCompat.Action(
            R.drawable.play_icon,
            getString(R.string.play),
            PendingIntent.getService(
                this,
                0,
                Intent(ACTION_PLAY_PAUSE),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val stopAction = NotificationCompat.Action(
            android.R.drawable.ic_media_pause,
            getString(R.string.stop),
            PendingIntent.getService(
                this,
                0,
                Intent(ACTION_STOP),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.audio)
            .setContentIntent(pendingIntent)
            .addAction(playPauseAction)
            .addAction(stopAction)
            .build()
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "com.example.myapp.ACTION_PLAY_PAUSE"
        const val ACTION_STOP = "com.example.myapp.ACTION_STOP"
        const val EXTRA_FILE_URI = "com.example.myapp.EXTRA_FILE_URI"
        const val EXTRA_FILE_PATH = "com.example.myapp.EXTRA_FILE_PATH"
        const val CHANNEL_ID = "com.example.myapp.CHANNEL_ID"
        const val CHANNEL_NAME = "AudioPlayer"
    }
}
