package com.hs.media.view

import androidx.recyclerview.widget.RecyclerView
import com.hs.media.databinding.LayoutThumbnailBinding
import com.hs.media.utils.ThumbnailClickListner
import java.io.File

//import com.squareup.picasso.Picasso

class GallaryFragmentViewHolder(private val binding: LayoutThumbnailBinding, private val listener: ThumbnailClickListner): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: String) {
//        binding.placeName.text = item.name
//        binding.rating.text = item.rating.toString()
//        binding.typeAndAddress.text="${item.type}-${item.vicinity}"
//        binding.status.text= item.open
//        binding.lat.text = item.lat.toString()
//        binding.lng.text = item.lng.toString()
//
//        Picasso.with(binding.root.context).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photo_reference=${item.photoRef}&key=AIzaSyAvhpYgskGiCMYUTcYJ-xFAKdfvo_Yf65E").into(binding.placeImage)
////        binding.activityListProductViewDetails.setOnClickListener {
////            listener.onItemClicked(item.id)
////        }
    }
}