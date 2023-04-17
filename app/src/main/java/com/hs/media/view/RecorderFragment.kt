package com.hs.media.view

import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hs.media.R
import com.hs.media.databinding.FragmentRecorderBinding
import com.hs.media.utils.Capture
import com.hs.media.utils.Permissions
import java.io.File
import java.io.FileOutputStream

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RecorderFragment : Fragment() {

    private var _binding: FragmentRecorderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isRecording: Boolean = false
    private var recordingStartTime: Long = 0L // Timestamp of recording start time
    private val handler = Handler()
    private var elapsedMillis: Long = 0L // Elapsed time in milliseconds

    private var mediaRecorder: MediaRecorder? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecorderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
        if(!isRecording){
            if(Permissions.isAudioRecordPermissionGranted(requireContext())){
                    Capture.createAudioFile(requireContext()).let {

                        startRecording(it)
                        binding.buttonSecond.text = "End"
                    }
                    isRecording=true
                }
            }
            else{
                stopRecording()
                binding.buttonSecond.text = "Start"
            }
        }
    }
    private fun startRecording(outputFile:File) {
     Capture.getRecorder(requireContext()).apply {
         setAudioSource(MediaRecorder.AudioSource.MIC)
         setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
         setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
         setOutputFile(FileOutputStream(outputFile).fd)

         prepare()
         start()

         mediaRecorder = this
     }
        // Update recording start time and set isRecording flag to true
        recordingStartTime = System.currentTimeMillis()
        isRecording = true

        // Start updating elapsed time periodically with a Handler
        handler.postDelayed(updateElapsedTimeRunnable, 1000)
    }
    private val updateElapsedTimeRunnable = object : Runnable {
        override fun run() {
            // Calculate elapsed time
            elapsedMillis = System.currentTimeMillis() - recordingStartTime

            // Update UI with elapsed time, e.g., display it in a TextView
            val elapsedSeconds = elapsedMillis / 1000
            val minutes = elapsedSeconds / 60
            val seconds = elapsedSeconds % 60
            val elapsedTimeString = String.format("%02d:%02d", minutes, seconds)
            binding.textViewElapsedTime.text = elapsedTimeString

            // Post the runnable again after 1 second (1000 milliseconds)
            handler.postDelayed(this, 1000)
        }
    }
    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording=false
        handler.removeCallbacks(updateElapsedTimeRunnable);
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}