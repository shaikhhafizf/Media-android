
package com.hs.media.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils

object Permissions {
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
    const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 200
    const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 300
    @SuppressLint("RestrictedApi")
    fun isCameraPermissionGranted(context: Context): Boolean {
        return if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            true
        } else{
            //asking permission
            ContextUtils.getActivity(context)?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
            false
        }
    }
    @SuppressLint("RestrictedApi")
    fun isWritePermissionGranted(context: Context):Boolean{
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
            true
        }
        else{// Request permission
            ContextUtils.getActivity(context)?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
            false
        }
    }
    @SuppressLint("RestrictedApi")
    fun isReadPermissionGranted(context: Context):Boolean{
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
            true
        }
        else{// Request permission
            ContextUtils.getActivity(context)?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
            false
        }
    }
    @SuppressLint("RestrictedApi")
    fun isAudioRecordPermissionGranted(context: Context):Boolean{
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED){
            true
        }
        else{// Request permission
            ContextUtils.getActivity(context)?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.RECORD_AUDIO,),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
            false
        }
    }
    @SuppressLint("RestrictedApi")
    fun isNotificationPermissionGranted(context: Context):Boolean{
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED){
            true
        }
        else{// Request permission
            ContextUtils.getActivity(context)?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS,),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
            false
        }
    }

}