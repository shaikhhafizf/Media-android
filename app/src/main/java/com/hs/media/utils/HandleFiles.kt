package com.hs.media.utils

import android.content.Context
import android.os.Environment
import android.provider.Settings
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object HandleFiles {
fun getImageFolderLocation(context:Context):String{

    // Notify the media scanner to add the image file to the gallery
    if(Settings.System.canWrite(context)){

        val destinationDirectory = File(Environment.getDataDirectory(), "Media")
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs()
        }

        return destinationDirectory.toString()
    }
    else{
        return ""
    }
}
    fun moveFile(targetFile: String, destinationFile: String) {
        val sourcePath = Paths.get(targetFile)
        val targetPath = Paths.get(destinationFile)
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
    }

}