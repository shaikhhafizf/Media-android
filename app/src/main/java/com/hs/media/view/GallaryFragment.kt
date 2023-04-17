package com.hs.media.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.hs.hafizshaikh_mapd721_optionalassignment.view.GallaryFragmentAdapter
import com.hs.media.R
import com.hs.media.databinding.FragmentGallaryBinding
import com.hs.media.utils.Capture
import com.hs.media.utils.Permissions
import com.hs.media.utils.ThumbnailClickListner
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GallaryFragment : Fragment(),ThumbnailClickListner {

    private var _binding: FragmentGallaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Initializing list adaptor
    private lateinit var adapter: GallaryFragmentAdapter

    companion object{
        lateinit var takePicture: ActivityResultLauncher<Intent>
        private const val VIDEO_CAPTURE_REQUEST_CODE = 1001
    }

    val videoCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Handle the recorded video here, if needed
            val videoUri = result.data?.data
            // Do something with the videoUri, e.g., display it in a VideoView or upload it to a server
            adapter.updateAdapterData()
            Toast.makeText(
                requireContext(),
                "Video captured successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    val imageCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Handle the recorded video here, if needed
            val videoUri = result.data?.data
            // Do something with the videoUri, e.g., display it in a VideoView or upload it to a server
            adapter.updateAdapterData()
            Toast.makeText(
                requireContext(),
                "Image captured successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Initializing adapter
        adapter = GallaryFragmentAdapter(this,requireContext())
        adapter.updateAdapterData()


        takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap = BitmapFactory.decodeFile(Capture.currentPhotoPath, options)
                val imageFile = File(Capture.currentPhotoPath)
                adapter.updateAdapterData()
                MediaScannerConnection.scanFile(
                    requireContext(),
                    arrayOf(imageFile.toString()),
                    null
                ) { path, uri ->
                    // Media scanning completed
                    // The photo is now visible in the Gallery app
                    Toast.makeText(requireContext(), "Scanned", Toast.LENGTH_SHORT).show()
                }

            }
        }


        _binding = FragmentGallaryBinding.inflate(inflater, container, false)
        //assigning Adapter
        binding.gridView.adapter = adapter
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clickVideoOrPic.setOnClickListener {

            if (binding.clickVideo.visibility == View.VISIBLE) {
                binding.clickVideo.visibility = View.GONE // Set to invisible
                binding.clickPic.visibility = View.GONE
            } else {
                binding.clickVideo.visibility = View.VISIBLE // Set to visible
                binding.clickPic.visibility = View.VISIBLE
            }
        }
        binding.clickVideo.setOnClickListener{
            Capture.captureVideo(view.context,videoCaptureLauncher)
        }
        binding.clickPic.setOnClickListener{
            Capture.captureImage(view.context,imageCaptureLauncher)
        }
        binding.recordVoice.setOnClickListener {
            if(Permissions.isNotificationPermissionGranted(requireContext())){

                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }


    private fun recordVideo() {
        // Create an intent to capture video

        if(Permissions.isCameraPermissionGranted(requireContext())) {

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            videoCaptureLauncher.launch(intent)
        }
    }
    private fun captureImage(){
        if(Permissions.isCameraPermissionGranted(requireContext())) {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageCaptureLauncher.launch(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickCard(id: String) {
        Toast.makeText(context, "Camera permission is not given", Toast.LENGTH_SHORT).show()
    }
}