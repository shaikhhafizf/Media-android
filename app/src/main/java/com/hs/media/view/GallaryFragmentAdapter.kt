package com.hs.hafizshaikh_mapd721_optionalassignment.view

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hs.media.R
import com.hs.media.utils.HandleFiles.decodeSampledBitmapFromUri
import com.hs.media.utils.ThumbnailClickListner
import java.io.File

class GallaryFragmentAdapter(private val listener: ThumbnailClickListner, private val context: Context) : BaseAdapter() {

    private var itemList = ArrayList<File>()
    val filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

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
            layoutParams.width = parent.measuredWidth / 3 - 16 // Adjust this value based on the number of columns in your grid
            layoutParams.height = layoutParams.width
            view.layoutParams = layoutParams
        }

        val imv: ImageView = view?.findViewById<ImageView>(R.id.pic)!!
        val brokenImgIcon: ImageView = view?.findViewById<ImageView>(R.id.brokenImgIcon)!!
        if(isJpegFileValid(itemList[position])){

            imv.setImageBitmap(decodeSampledBitmapFromUri(itemList[position].absolutePath, 220, 220))
            imv.alpha = 1.0f
        }
        else if(isMp4FileValid(itemList[position])){

        }
        else{
            brokenImgIcon.visibility = View.VISIBLE;
        }
        return view
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
    private fun isMp4FileValid(file: File): Boolean {
        return try {
            // Attempt to decode the image file
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth > 0 && options.outHeight > 0
        } catch (e: Exception) {
            // Handle any exceptions that may occur during image decoding
            false
        }
    }

    private fun isJpegFileValid(file: File): Boolean {
        return try {
            // Attempt to decode the image file
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth > 0 && options.outHeight > 0
        } catch (e: Exception) {
            // Handle any exceptions that may occur during image decoding
            false
        }
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