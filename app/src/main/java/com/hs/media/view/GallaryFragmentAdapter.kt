package com.hs.hafizshaikh_mapd721_optionalassignment.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.hs.media.R
import com.hs.media.utils.HandleFiles.decodeSampledBitmapFromUri
import com.hs.media.utils.ThumbnailClickListner
import com.hs.media.view.AudioPlayerActivity
import com.hs.media.view.ImageViewerActivity
import com.hs.media.view.VideoPlayerActivity
import java.io.File

class GallaryFragmentAdapter(private val listener: ThumbnailClickListner, private val context: Context) : BaseAdapter() {

    private var itemList = ArrayList<File>()
    val filePath = context.getExternalFilesDir("Media")

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)

            view = inflater.inflate(R.layout.layout_thumbnail, parent, false)

            // Set width and height to be the same, so that each item appears as a square
            val layoutParams = view.layoutParams
            parent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    layoutParams.width = parent.measuredWidth / 3 - 16 // Adjust this value based on the number of columns in your grid
                    layoutParams.height = layoutParams.width
                    view.layoutParams = layoutParams
                }
            })
        }

        val imv: ImageView = view?.findViewById<ImageView>(R.id.pic)!!
        val brokenImgIcon: ImageView = view?.findViewById<ImageView>(R.id.brokenImgIcon)!!
        val audioIcon: ImageView = view?.findViewById<ImageView>(R.id.audioIcon)!!
        val videoIcon: ImageView = view?.findViewById<ImageView>(R.id.videoIcon)!!
        if(isFileValid(itemList[position].toString())){
            getFileType(itemList[position].toString()).let{
                when (it){
                    "Image" ->{
                        imv.setImageBitmap(decodeSampledBitmapFromUri(itemList[position].absolutePath, 220, 220))
                        imv.alpha = 1.0f

                        // Set an OnClickListener on the view
                        view.setOnClickListener {
                            // Create an intent to start the ImageViewerActivity
                            val intent = Intent(context, ImageViewerActivity::class.java)

                            // Pass the image file path as an extra to the intent
                            intent.putExtra("imagePath", itemList[position].absolutePath)

                            // Start the ImageViewerActivity
                            context.startActivity(intent)
                        }
                    }
                    "Audio" ->{
//                        imv.visibility = View.GONE
                        imv.alpha = 0.2f
                        audioIcon.visibility = View.VISIBLE
                        // Set an OnClickListener on the view
                        view.setOnClickListener {
                            // Handle item click event here
                            val intent = Intent(context, AudioPlayerActivity::class.java)
                            intent.putExtra("FILE_URI", FileProvider.getUriForFile(
                                context,
                                "com.hs.media.fileprovider", // Replace with your app's file provider authority
                                itemList[position]
                            ))
                            context.startActivity(intent)
                        }
                    }
                    "Video" ->{
                        imv.alpha = 0.7f
                        videoIcon.visibility = View.VISIBLE
                        imv.setImageBitmap(getVideoThumbnailFromFile(itemList[position]))
                        // Set an OnClickListener on the view
                        view.setOnClickListener {
                            // Handle item click event here
                            val intent = Intent(context, VideoPlayerActivity::class.java)
                            intent.putExtra("FILE_URI", FileProvider.getUriForFile(
                                context,
                                "com.hs.media.fileprovider", // Replace with your app's file provider authority
                                itemList[position]
                            ))
                            context.startActivity(intent)
                        }
                    }
                    "Unknown" ->{
                        brokenImgIcon.visibility = View.VISIBLE;
                        view.setOnClickListener {
                            // Handle item click event here
                            Toast.makeText(context, "Cannot open this file as it is carshed", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
        else{
            brokenImgIcon.visibility = View.VISIBLE;
            view.setOnClickListener {
                // Handle item click event here
                Toast.makeText(context, "Cannot open this file as it is carshed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    fun getVideoThumbnailFromFile(videoFile: File): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            // Set the data source to the video file path
            retriever.setDataSource(videoFile.absolutePath)

            // Get the thumbnail bitmap from the retriever
            val thumbnail = retriever.getFrameAtTime()

            // Return the thumbnail bitmap
            return thumbnail
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
        } finally {
            // Release the MediaMetadataRetriever
            retriever.release()
        }

        return null
    }

    fun updateAdapterData() {
        itemList = ArrayList<File>()
        notifyDataSetChanged()
        val files = filePath?.listFiles()?.reversedArray()
        if (files != null) {
            itemList = ArrayList<File>()
            for (file in files) {
                itemList.add(file)
            }
        }
        this.notifyDataSetChanged()
        // Call notifyDataSetChanged on UI thread
        (context as? Activity)?.runOnUiThread {
            notifyDataSetChanged()
        }
    }
    fun getFileType(filePath: String): String {
        // Get the file extension from the file path
        val fileExtension = File(filePath).extension

        // Return the file type based on the file extension
        return when (fileExtension.toLowerCase()) {
            "jpg", "jpeg", "png", "gif" -> "Image"
            "3gp" -> "Audio"
            "mp4" -> "Video"
            else -> "Unknown"
        }
    }

    fun isFileValid(filePath: String): Boolean {
        val file = File(filePath)

        // Check if the file exists
        if (!file.exists()) {
            return false
        }

        // Check if the file is not empty
        if (file.length() <= 0) {
            return false
        }

        return true
    }
}
//(private val listener: ThumbnailClickListner,context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = LayoutThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return GallaryFragmentViewHolder(binding, listener)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as GallaryFragmentViewHolder).bind(itemList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//
//
//}