package com.hs.media.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.hs.hafizshaikh_mapd721_optionalassignment.view.GallaryFragmentAdapter
import com.hs.media.R
import com.hs.media.databinding.FragmentGallaryBinding
import com.hs.media.utils.Capture
import com.hs.media.utils.ThumbnailClickListner
import java.io.File

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
            Capture.captureImage(view.context,GallaryFragment.takePicture)
//            Snackbar.make(it, "Getting Picture", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
        binding.recordVoice.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
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