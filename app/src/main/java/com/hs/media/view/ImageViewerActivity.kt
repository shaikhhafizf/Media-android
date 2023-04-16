package com.hs.media.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hs.media.R

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        // Retrieve the image resource ID from the intent extra
        val imageResId = intent.getIntExtra("imageResId", 0)


        // Find the Toolbar view by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // Set the Toolbar as the action bar
        setSupportActionBar(toolbar)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Get a reference to the VideoView in the layout
        val imageView = findViewById<ImageView>(R.id.imageView)
        // Retrieve the image file path from the intent extra
        val imagePath = intent.getStringExtra("imagePath")

        // Load the image from the file path and set it to the ImageView
        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageView.setImageBitmap(bitmap)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle the back button click event
        onBackPressed()
        return true
    }
}
