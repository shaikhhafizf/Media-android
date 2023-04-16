package com.hs.media.view

import android.media.AudioRecord
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.MediaController
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.hs.media.databinding.AudioPlayerActivityBinding
import com.hs.media.databinding.FragmentGallaryBinding

class AudioPlayerActivity: AppCompatActivity() {

    private var binding: AudioPlayerActivityBinding? = null
    private var mediaPlayer:MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AudioPlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        // Set the path of the video file to be played
        val audioUri = intent.getParcelableExtra<Uri>("FILE_URI")
        if (audioUri != null) {
            val packageName = packageName
            val audioUri = Uri.parse("android.resource://$packageName/${audioUri.path}")
            // Now you can use the videoUri to load the video
        }

        val mediaPlayer = mediaPlayer as? MediaPlayer
        if (mediaPlayer != null) {
            // Perform operations on mediaPlayer
            mediaPlayer.start()
        }



    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release media player resources when the activity is destroyed
        mediaPlayer?.release()
        mediaPlayer = null
    }
}