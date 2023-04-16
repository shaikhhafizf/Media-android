package com.hs.media.utils

import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Capture {
    var currentPhotoPath: String? = null

    fun captureImage(context: Context, takePicture: ActivityResultLauncher<Intent>){
        if(Permissions.isCameraPermissionGranted(context)) {
            // Function to be triggered after camera permission is granted
            // Add your logic here
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile(context)
            } catch (ex: IOException) {
                // Handle error while creating the file
                null
            }

            photoFile?.let {
                val photoURI = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePicture.launch(takePictureIntent)
            }
        }
        else{
            Toast.makeText(context, "Camera permission is not given", Toast.LENGTH_SHORT).show()
        }

    }
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Media")
//        val storageDir = File(Environment.getExternalStorageDirectory(), "Media")
//        storageDir.mkdirs() // Create the directory if it doesn't exist
//        val imageFile = File(storageDir, "JPEG_${timeStamp}_.jpg")
        val imageFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }
    fun captureVideo(context: Context, takePicture: ActivityResultLauncher<Intent>){
        if(Permissions.isCameraPermissionGranted(context)) {
            // Function to be triggered after camera permission is granted
            // Add your logic here
            val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val photoFile: File? = try {
                createVideoFile(context)
            } catch (ex: IOException) {
                // Handle error while creating the file
                null
            }

            photoFile?.let {
                val photoURI = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePicture.launch(takePictureIntent)
            }
        }
        else{
            Toast.makeText(context, "Camera permission is not given", Toast.LENGTH_SHORT).show()
        }

    }
    private fun createVideoFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Media")
        val imageFile = File.createTempFile(
            "VID_${timeStamp}",
            ".mp4",
            storageDir
        )
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    fun createAudioFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Media")
        val imageFile = File.createTempFile(
            "AUD_${timeStamp}",
            ".3gp",
            storageDir
        )
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }
    fun getRecorder(context: Context):MediaRecorder{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        }else MediaRecorder()
    }

}