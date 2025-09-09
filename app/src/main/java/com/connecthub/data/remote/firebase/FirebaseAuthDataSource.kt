package com.connecthub.data.remote.firebase

import android.app.Activity
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
    
    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
    
    fun isAnonymousUser(): Boolean = firebaseAuth.currentUser?.isAnonymous == true
    
    suspend fun signInAnonymously(): Result<FirebaseUser> {
        return try {
            Timber.d("Signing in anonymously")
            val result = firebaseAuth.signInAnonymously().await()
            result.user?.let { user ->
                Timber.d("Anonymous sign-in successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("Anonymous user is null"))
        } catch (e: Exception) {
            Timber.e(e, "Anonymous sign-in failed")
            Result.failure(e)
        }
    }
    
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            Timber.d("Signing in with email: $email")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                Timber.d("Email sign-in successful: ${user.uid}")
                Result.success(user) 
            } ?: Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Timber.e(e, "Email sign-in failed")
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            Timber.d("Creating user with email: $email")
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                Timber.d("User creation successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Timber.e(e, "User creation failed")
            Result.failure(e)
        }
    }
    
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            Timber.d("Signing in with Google")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { user ->
                Timber.d("Google sign-in successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("Google user is null"))
        } catch (e: Exception) {
            Timber.e(e, "Google sign-in failed")
            Result.failure(e)
        }
    }
    
    suspend fun linkAnonymousWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser?.isAnonymous == true) {
                Timber.d("Linking anonymous account with email: $email")
                val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)
                val result = currentUser.linkWithCredential(credential).await()
                result.user?.let { user ->
                    Timber.d("Anonymous account linked successfully: ${user.uid}")
                    Result.success(user)
                } ?: Result.failure(Exception("Linked user is null"))
            } else {
                Result.failure(Exception("Current user is not anonymous"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to link anonymous account")
            Result.failure(e)
        }
    }
    
    suspend fun linkAnonymousWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser?.isAnonymous == true) {
                Timber.d("Linking anonymous account with Google")
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = currentUser.linkWithCredential(credential).await()
                result.user?.let { user ->
                    Timber.d("Anonymous account linked with Google successfully: ${user.uid}")
                    Result.success(user)
                } ?: Result.failure(Exception("Linked Google user is null"))
            } else {
                Result.failure(Exception("Current user is not anonymous"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to link anonymous account with Google")
            Result.failure(e)
        }
    }
    
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            currentUser?.let { user ->
                Timber.d("Deleting user account: ${user.uid}")
                user.delete().await()
                Timber.d("Account deleted successfully")
                Result.success(Unit)
            } ?: Result.failure(Exception("No current user to delete"))
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete account")
            Result.failure(e)
        }
    }
    
    fun sendPhoneVerificationCode(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        try {
            Timber.d("Sending phone verification code to: $phoneNumber")
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            Timber.e(e, "Failed to send phone verification code")
        }
    }
    
    suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<FirebaseUser> {
        return try {
            Timber.d("Verifying phone number with code")
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { user ->
                Timber.d("Phone verification successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("Phone verification user is null"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Timber.e(e, "Invalid verification code")
            Result.failure(Exception("Invalid verification code"))
        } catch (e: Exception) {
            Timber.e(e, "Phone verification failed")
            Result.failure(e)
        }
    }
    
    suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser> {
        return try {
            Timber.d("Signing in with phone credential")
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { user ->
                Timber.d("Phone credential sign-in successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("Phone credential user is null"))
        } catch (e: Exception) {
            Timber.e(e, "Phone credential sign-in failed")
            Result.failure(e)
        }
    }
    
    suspend fun linkAnonymousWithPhone(credential: PhoneAuthCredential): Result<FirebaseUser> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser?.isAnonymous == true) {
                Timber.d("Linking anonymous account with phone")
                val result = currentUser.linkWithCredential(credential).await()
                result.user?.let { user ->
                    Timber.d("Anonymous account linked with phone successfully: ${user.uid}")
                    Result.success(user)
                } ?: Result.failure(Exception("Linked phone user is null"))
            } else {
                Result.failure(Exception("Current user is not anonymous"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to link anonymous account with phone")
            Result.failure(e)
        }
    }
    
    suspend fun signInWithFacebook(accessToken: AccessToken): Result<FirebaseUser> {
        return try {
            Timber.d("Signing in with Facebook")
            val credential = FacebookAuthProvider.getCredential(accessToken.token)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { user ->
                Timber.d("Facebook sign-in successful: ${user.uid}")
                Result.success(user)
            } ?: Result.failure(Exception("Facebook user is null"))
        } catch (e: Exception) {
            Timber.e(e, "Facebook sign-in failed")
            Result.failure(e)
        }
    }
    
    suspend fun linkAnonymousWithFacebook(accessToken: AccessToken): Result<FirebaseUser> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser?.isAnonymous == true) {
                Timber.d("Linking anonymous account with Facebook")
                val credential = FacebookAuthProvider.getCredential(accessToken.token)
                val result = currentUser.linkWithCredential(credential).await()
                result.user?.let { user ->
                    Timber.d("Anonymous account linked with Facebook successfully: ${user.uid}")
                    Result.success(user)
                } ?: Result.failure(Exception("Linked Facebook user is null"))
            } else {
                Result.failure(Exception("Current user is not anonymous"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to link anonymous account with Facebook")
            Result.failure(e)
        }
    }
    
    fun getGoogleSignInClient(activity: Activity, webClientId: String): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }
    
    suspend fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>): Result<FirebaseUser> {
        return try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                Timber.d("Google Sign-In successful, proceeding with Firebase auth")
                signInWithGoogle(idToken)
            } ?: Result.failure(Exception("Google Sign-In failed: No ID token"))
        } catch (e: ApiException) {
            Timber.e(e, "Google Sign-In failed")
            Result.failure(e)
        }
    }
    
    suspend fun handleGoogleSignInResultForLinking(task: Task<GoogleSignInAccount>): Result<FirebaseUser> {
        return try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                Timber.d("Google Sign-In successful, proceeding with Firebase linking")
                linkAnonymousWithGoogle(idToken)
            } ?: Result.failure(Exception("Google Sign-In failed: No ID token"))
        } catch (e: ApiException) {
            Timber.e(e, "Google Sign-In failed")
            Result.failure(e)
        }
    }
    
    fun signOut() {
        val currentUser = firebaseAuth.currentUser
        Timber.d("Signing out user: ${currentUser?.uid}")
        firebaseAuth.signOut()
    }
    
    fun signOutFromGoogle(googleSignInClient: GoogleSignInClient?) {
        signOut()
        googleSignInClient?.signOut()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("Google sign-out successful")
            } else {
                Timber.e("Google sign-out failed")
            }
        }
    }
}