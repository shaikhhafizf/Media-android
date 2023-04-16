package com.hs.media.view

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.hs.media.R
import com.hs.media.services.AudioPlayerService
import java.io.File

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var ivAlbumArt: ImageView
    private lateinit var tvSongTitle: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPrevious: Button
    private lateinit var btnPlayPause: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player_activity)

        ivAlbumArt = findViewById(R.id.ivAlbumArt)
        tvSongTitle = findViewById(R.id.tvSongTitle)
        seekBar = findViewById(R.id.seekBar)
//        btnPrevious = findViewById(R.id.btnPrevious)
        btnPlayPause = findViewById(R.id.btnPlayPause)
//        btnNext = findViewById(R.id.btnNext)


        // Find the Toolbar view by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // Set the Toolbar as the action bar
        setSupportActionBar(toolbar)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the file URI or file path from the intent
        val fileUri = intent.getParcelableExtra<Uri>("File")
        val filePath = intent.getStringExtra("File")

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, fileUri ?: Uri.fromFile(File(filePath)))
        mediaPlayer.prepare()

//        btnPrevious.setOnClickListener {
//            // Handle previous button click
//        }

        btnPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                btnPlayPause.text = getString(R.string.play)
            } else {
                mediaPlayer.start()
                btnPlayPause.text = getString(R.string.pause)
                updateSeekBar()

//                val intent = Intent(this, AudioPlayerService::class.java)
//                intent.putExtra("File", fileUri) // Pass the file URI as an extra
//                intent.putExtra("File", filePath) // Pass the file path as an extra
//
//                // Start the service with the intent
//                startService(intent)
            }
        }

//        btnNext.setOnClickListener {
//            // Handle next button click
//        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Handle seek bar progress change
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle seek bar touch start
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle seek bar touch stop
            }
        })

    }

    private fun updateSeekBar() {
        seekBar.progress = mediaPlayer.currentPosition
        if (mediaPlayer.isPlaying) {
            seekBar.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
