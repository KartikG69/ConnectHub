package com.connecthub.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun <T> addDocument(collection: String, data: T): Result<String> {
        return try {
            val docRef = firestore.collection(collection).add(data!!).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun <T> getDocument(collection: String, documentId: String, clazz: Class<T>): Result<T?> {
        return try {
            val document = firestore.collection(collection).document(documentId).get().await()
            val data = document.toObject(clazz)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun <T> updateDocument(collection: String, documentId: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection(collection).document(documentId).update(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteDocument(collection: String, documentId: String): Result<Unit> {
        return try {
            firestore.collection(collection).document(documentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}