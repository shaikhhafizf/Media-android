package com.hs.media.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hs.media.R
import com.hs.media.databinding.AudioPlayerActivityBinding
import com.hs.media.utils.Permissions
import java.io.File

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private var audioDuration = 0
    private var currentPlayTime = 0
    private var handler: Handler? = null
    private var isPlaying = false

    lateinit var binding:AudioPlayerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the Toolbar view by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // Set the Toolbar as the action bar
        setSupportActionBar(toolbar)

        registerReceiver(AudioPlayerServiceBroadcastReceiver(), IntentFilter().apply {
            addAction("ACTION_PLAY")
            addAction("ACTION_PAUSE")
        })
        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the file URI or file path from the intent
        val fileUri = intent.getParcelableExtra<Uri>("File")
        val filePath = intent.getStringExtra("File")

        mediaPlayer = MediaPlayer()
        val File = File(filePath)
        mediaPlayer.setDataSource(this, fileUri ?: Uri.fromFile(File))
        mediaPlayer.prepare()
        binding.tvSongTitle.text = File.name

        // Get audio duration
        audioDuration = mediaPlayer?.duration ?: 0

        // Set up UI
        binding.seekBar.max = audioDuration
        updatePlayTime(0)
        updatePlayButton()

        binding.btnPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "audioPlayerChannelId"
            val channelName = "Audio Player Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updatePlayTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No-op
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    val progress = it.progress
                    mediaPlayer?.seekTo(progress)
                    updatePlayTime(progress)
                }
            }
        })

    }

    private fun playAudio() {
        mediaPlayer?.start()
        isPlaying = true
        updatePlayButton()
        startAudioProgressUpdate()
        showNotification()
//        sendBroadcast(NotificationService.ACTION_PLAY)
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
        isPlaying = false
        updatePlayButton()
        stopAudioProgressUpdate()
//        sendBroadcast(NotificationService.ACTION_PAUSE)
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.seekTo(0)
        isPlaying = false
        updatePlayButton()
        stopAudioProgressUpdate()
        // Cancel the notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(1)
//        sendBroadcast(NotificationService.ACTION_STOP)
    }

    private fun startAudioProgressUpdate() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                currentPlayTime = mediaPlayer?.currentPosition ?: 0
                updatePlayTime(currentPlayTime)
                handler.postDelayed(this, 1000) // Update every 1 second
            }
        })
    }
    private fun updateAudioProgress() {
        handler?.postDelayed({
            // Update play time logic here
            updateAudioProgress()
        }, 1000)
    }

    private fun stopAudioProgressUpdate() {
        // Remove callbacks from handler to stop updating play time
        handler?.removeCallbacksAndMessages(null)
    }

    private fun updatePlayButton() {
        if (isPlaying) {
            binding.btnPlayPause.text = "Pause"
        } else {
            binding.btnPlayPause.text = "Play"
        }
    }

    private fun updatePlayTime(playTime: Int) {
        // Update seek bar and play time text view
        binding.seekBar.progress = playTime
        val minutes = playTime / 1000 / 60
        val seconds = playTime / 1000 % 60
//        playTimeTextView.text = String.format("%02d:%02d", minutes, seconds)
    }

    fun showNotification(){
        // Create a PendingIntent for the play action
        val playPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("ACTION_PLAY"),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

// Create a PendingIntent for the pause action
        val pausePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("ACTION_PAUSE"),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

// Create the play/pause button
        val playPauseAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_pause, "Pause", pausePendingIntent
        ).build()
        // Create a notification
        val notificationBuilder = NotificationCompat.Builder(this, "audioPlayerChannelId")
            .setContentTitle("Audio Player")
            .setContentText("Playing audio")
            .setSmallIcon(android.R.drawable.presence_audio_away)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setColor(ContextCompat.getColor(this, R.color.white))
//            .addAction(playPauseAction)

// Show the notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (Permissions.isNotificationPermissionGranted(this)){
            notificationManager.notify(1, notificationBuilder.build())
        }

    }

    private fun sendBroadcast(action: String) {
        val intent = Intent(action)
        intent.putExtra("audioDuration", audioDuration)
        intent.putExtra("currentPlayTime", currentPlayTime)
        sendBroadcast(intent)
    }

    inner class AudioPlayerServiceBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    "ACTION_PLAY" -> {
                        val test = "test"
                    }
                    "ACTION_PAUSE" -> {
                        val test = "test"
                    }
                }
            }
        }
    }
    private fun updateSeekBar() {
        binding.seekBar.progress = mediaPlayer.currentPosition
        if (mediaPlayer.isPlaying) {
            binding.seekBar.postDelayed({ updateSeekBar() }, 1000)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
        mediaPlayer?.release()
    }
}
