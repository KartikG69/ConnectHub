package com.connecthub.presentation.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connecthub.data.remote.firebase.FirebaseAuthDataSource
import com.connecthub.utils.constants.FirebaseConstants
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val currentUser: FirebaseUser? = null,
    val errorMessage: String? = null,
    val verificationId: String? = null,
    val phoneNumber: String = "",
    val isPhoneVerificationInProgress: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already authenticated
        checkAuthState()
    }
    
    private fun checkAuthState() {
        val currentUser = firebaseAuthDataSource.getCurrentUser()
        _uiState.value = _uiState.value.copy(
            isAuthenticated = currentUser != null,
            currentUser = currentUser
        )
    }
    
    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = firebaseAuthDataSource.signInAnonymously()
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUser = user
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Anonymous sign-in failed"
                    )
                }
            )
        }
    }
    
    fun sendPhoneVerificationCode(phoneNumber: String, activity: Activity) {
        Timber.d("📱 Starting phone verification for: $phoneNumber")
        _uiState.value = _uiState.value.copy(
            isLoading = true, 
            errorMessage = null,
            phoneNumber = phoneNumber,
            isPhoneVerificationInProgress = true
        )
        
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                Timber.d("✅ Phone verification completed automatically")
                viewModelScope.launch {
                    val result = firebaseAuthDataSource.signInWithPhoneCredential(credential)
                    result.fold(
                        onSuccess = { user ->
                            Timber.d("🎉 Auto phone sign-in successful: ${user.uid}")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isAuthenticated = true,
                                currentUser = user,
                                isPhoneVerificationInProgress = false
                            )
                        },
                        onFailure = { exception ->
                            Timber.e(exception, "❌ Auto phone sign-in failed")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = exception.message ?: "Phone verification failed",
                                isPhoneVerificationInProgress = false
                            )
                        }
                    )
                }
            }
            
            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                Timber.e(e, "❌ Phone verification failed: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Phone verification failed",
                    isPhoneVerificationInProgress = false
                )
            }
            
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Timber.d("📨 Verification code sent successfully! VerificationId: ${verificationId.take(10)}...")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    verificationId = verificationId,
                    isPhoneVerificationInProgress = true
                )
            }
        }
        
        try {
            firebaseAuthDataSource.sendPhoneVerificationCode(phoneNumber, activity, callbacks)
            Timber.d("🚀 Phone verification request sent to Firebase")
        } catch (e: Exception) {
            Timber.e(e, "💥 Failed to send phone verification request")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to send verification code: ${e.message}",
                isPhoneVerificationInProgress = false
            )
        }
    }
    
    fun verifyPhoneCode(code: String) {
        Timber.d("🔐 Attempting to verify OTP code: $code")
        val verificationId = _uiState.value.verificationId
        val currentState = _uiState.value
        
        Timber.d("📋 Current Auth State - VerificationId: ${verificationId?.take(10)}..., isPhoneVerificationInProgress: ${currentState.isPhoneVerificationInProgress}")
        
        if (verificationId == null) {
            Timber.e("❌ No verification ID available! Cannot verify code.")
            Timber.d("🔍 Debug: Current phone number: ${currentState.phoneNumber}")
            _uiState.value = _uiState.value.copy(errorMessage = "No verification ID available. Please request code again.")
            return
        }
        
        if (code.length != 6) {
            Timber.e("❌ Invalid code length: ${code.length}. Expected 6 digits.")
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a 6-digit code")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            Timber.d("🚀 Calling Firebase to verify code...")
            
            val result = firebaseAuthDataSource.verifyPhoneNumberWithCode(verificationId, code)
            result.fold(
                onSuccess = { user ->
                    Timber.d("🎉 Phone verification successful! User: ${user.uid}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUser = user,
                        isPhoneVerificationInProgress = false
                    )
                },
                onFailure = { exception ->
                    Timber.e(exception, "❌ Phone code verification failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Code verification failed"
                    )
                }
            )
        }
    }
    
    fun signInWithGoogle(task: Task<GoogleSignInAccount>) {
        Timber.d("🔍 Processing Google Sign-In result...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = firebaseAuthDataSource.handleGoogleSignInResult(task)
            result.fold(
                onSuccess = { user ->
                    Timber.d("🎉 Google sign-in successful! User: ${user.uid}, Email: ${user.email}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUser = user
                    )
                },
                onFailure = { exception ->
                    Timber.e(exception, "❌ Google sign-in failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Google sign-in failed"
                    )
                }
            )
        }
    }
    
    fun signInWithFacebook(accessToken: AccessToken) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = firebaseAuthDataSource.signInWithFacebook(accessToken)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUser = user
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Facebook sign-in failed"
                    )
                }
            )
        }
    }
    
    fun signOut() {
        firebaseAuthDataSource.signOut()
        _uiState.value = AuthUiState()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun resendPhoneCode(activity: Activity) {
        val phoneNumber = _uiState.value.phoneNumber
        if (phoneNumber.isNotEmpty()) {
            sendPhoneVerificationCode(phoneNumber, activity)
        }
    }
    
    fun getGoogleSignInClient(activity: Activity) = firebaseAuthDataSource.getGoogleSignInClient(
        activity,
        FirebaseConstants.WEB_CLIENT_ID
    )
    
    // TEST FUNCTIONS - Only use for development/testing
    
    fun enableTestMode() {
        if (!FirebaseConstants.ENABLE_TEST_MODE) return
        
        com.connecthub.utils.testing.TestAuthHelper.logTestInstructions()
    }
}