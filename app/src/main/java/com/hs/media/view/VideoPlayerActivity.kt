package com.hs.media.view

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hs.media.R

class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player_activity)

        // Find the Toolbar view by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // Set the Toolbar as the action bar
        setSupportActionBar(toolbar)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        // Get a reference to the VideoView in the layout
        val videoView = findViewById<VideoView>(R.id.videoView)

        // Set the path of the video file to be played
        val videoUri = intent.getParcelableExtra<Uri>("FILE_URI")
        if (videoUri != null) {
            val packageName = packageName
            val videoUri = Uri.parse("android.resource://$packageName/${videoUri.path}")
            // Now you can use the videoUri to load the video
        }
        videoView.setVideoURI(videoUri)

        // Create a MediaController to enable video playback controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Start the video playback
        videoView.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle the back button click event
        onBackPressed()
        return true
    }
}
