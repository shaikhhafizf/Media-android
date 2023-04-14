package com.hs.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hs.media.databinding.FragmentGallaryBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GallaryFragment : Fragment() {

    private var _binding: FragmentGallaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGallaryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clickVideoOrPic.setOnClickListener {
            Snackbar.make(it, "Getting Picture", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        binding.recordVoice.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}