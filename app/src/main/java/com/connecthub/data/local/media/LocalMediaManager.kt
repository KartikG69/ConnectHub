package com.connecthub.data.local.media

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaManager @Inject constructor(
    private val context: Context
) {
    
    private val mediaDir = File(context.filesDir, "media").apply { 
        if (!exists()) mkdirs() 
    }
    
    private val imagesDir = File(mediaDir, "images").apply { 
        if (!exists()) mkdirs() 
    }
    
    private val audioDir = File(mediaDir, "audio").apply { 
        if (!exists()) mkdirs() 
    }
    
    private val profilesDir = File(context.filesDir, "profiles").apply { 
        if (!exists()) mkdirs() 
    }
    
    suspend fun saveImage(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val file = File(imagesDir, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            Timber.d("Image saved: ${file.absolutePath}")
            Result.success(file.absolutePath)
        } catch (e: IOException) {
            Timber.e(e, "Failed to save image")
            Result.failure(e)
        }
    }
    
    suspend fun saveBitmap(bitmap: Bitmap, prefix: String = "img"): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileName = "${prefix}_${System.currentTimeMillis()}.jpg"
            val file = File(imagesDir, fileName)
            
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
            }
            
            Timber.d("Bitmap saved: ${file.absolutePath}")
            Result.success(file.absolutePath)
        } catch (e: IOException) {
            Timber.e(e, "Failed to save bitmap")
            Result.failure(e)
        }
    }
    
    suspend fun saveProfileImage(uri: Uri, userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileName = "profile_${userId}.jpg"
            val file = File(profilesDir, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            Timber.d("Profile image saved: ${file.absolutePath}")
            Result.success(file.absolutePath)
        } catch (e: IOException) {
            Timber.e(e, "Failed to save profile image")
            Result.failure(e)
        }
    }
    
    suspend fun saveAudioRecording(audioData: ByteArray): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileName = "audio_${System.currentTimeMillis()}.m4a"
            val file = File(audioDir, fileName)
            
            FileOutputStream(file).use { output ->
                output.write(audioData)
            }
            
            Timber.d("Audio saved: ${file.absolutePath}")
            Result.success(file.absolutePath)
        } catch (e: IOException) {
            Timber.e(e, "Failed to save audio")
            Result.failure(e)
        }
    }
    
    fun getMediaFile(path: String): File? {
        return try {
            val file = File(path)
            if (file.exists()) file else null
        } catch (e: Exception) {
            Timber.e(e, "Failed to get media file: $path")
            null
        }
    }
    
    fun deleteMediaFile(path: String): Boolean {
        return try {
            val file = File(path)
            if (file.exists()) {
                val deleted = file.delete()
                Timber.d("Media file deleted: $path, success: $deleted")
                deleted
            } else {
                Timber.w("Media file does not exist: $path")
                false
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete media file: $path")
            false
        }
    }
    
    fun getMediaSize(): Long {
        return try {
            mediaDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
        } catch (e: Exception) {
            Timber.e(e, "Failed to calculate media size")
            0L
        }
    }
    
    fun clearCache(): Boolean {
        return try {
            mediaDir.deleteRecursively()
            mediaDir.mkdirs()
            imagesDir.mkdirs()
            audioDir.mkdirs()
            Timber.d("Media cache cleared")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to clear media cache")
            false
        }
    }
}