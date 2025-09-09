package com.connecthub.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    
    suspend fun uploadFile(path: String, fileUri: Uri): Result<String> {
        return try {
            val storageRef: StorageReference = storage.reference.child(path)
            val uploadTask = storageRef.putFile(fileUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadBytes(path: String, bytes: ByteArray): Result<String> {
        return try {
            val storageRef: StorageReference = storage.reference.child(path)
            val uploadTask = storageRef.putBytes(bytes).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteFile(path: String): Result<Unit> {
        return try {
            val storageRef: StorageReference = storage.reference.child(path)
            storageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}