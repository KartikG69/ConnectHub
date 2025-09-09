package com.connecthub.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connecthub.presentation.auth.AuthViewModel
import com.connecthub.presentation.components.Avatar
import com.connecthub.presentation.components.SocialLoginButton
import com.connecthub.presentation.components.SocialLoginType
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.HeroGradient
import com.connecthub.presentation.theme.White
import com.connecthub.utils.constants.FirebaseConstants
import timber.log.Timber

@Composable
fun LoginScreen(
    onNavigateToPhoneAuth: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onFacebookSignIn: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.uiState.collectAsState()
    
    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Timber.tag("LoginScreen").d("🔍 Google Sign-In result: ${result.resultCode}")
        val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
        viewModel.signInWithGoogle(task)
    }
    
    // Navigate when authenticated
    if (authState.isAuthenticated && authState.currentUser != null) {
        Timber.tag("LoginScreen")
            .d("🎉 User authenticated, navigating to dashboard: ${authState.currentUser?.email}")
        onGoogleSignIn()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Hero Section (40% of screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(HeroGradient)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo/Avatar placeholder
                Avatar(
                    name = "ConnectHub",
                    size = 80.dp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Sign in to continue to ConnectHub",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        // Login Options Section (60% of screen)
        Column(
            modifier = Modifier
                .weight(0.6f)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Error message
            authState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Google Sign-In - Re-enabled with updated google-services.json
            SocialLoginButton(
                text = if (authState.isLoading) "Signing in..." else "Continue with Google",
                type = SocialLoginType.GOOGLE,
                onClick = {
                    if (!authState.isLoading) {
                        Timber.tag("LoginScreen").d("🔍 Attempting Google Sign-In...")
                        Timber.tag("LoginScreen")
                            .d("🔑 Web Client ID: ${FirebaseConstants.WEB_CLIENT_ID}")
                        try {
                            val signInClient = viewModel.getGoogleSignInClient(context as android.app.Activity)
                            val signInIntent = signInClient.signInIntent
                            Timber.tag("LoginScreen").d("🚀 Launching Google Sign-In intent")
                            googleSignInLauncher.launch(signInIntent)
                        } catch (e: Exception) {
                            Timber.tag("LoginScreen")
                                .e("💥 Google Sign-In setup failed: ${e.message}")
                            Timber.tag("LoginScreen").e(
                                "🔧 Web Client ID format check: ${
                                    FirebaseConstants.WEB_CLIENT_ID.contains(".apps.googleusercontent.com")}")
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SocialLoginButton(
                text = "Continue with Phone",
                type = SocialLoginType.PHONE,
                onClick = {
                    Timber.tag("LoginScreen").d("📱 Navigating to phone authentication")
                    onNavigateToPhoneAuth()
                },
                icon = Icons.Default.Phone
            )
            
            // Facebook login commented out until properly configured
            /*
            Spacer(modifier = Modifier.height(12.dp))
            
            SocialLoginButton(
                text = if (authState.isLoading) "Signing in..." else "Continue with Facebook",
                type = SocialLoginType.FACEBOOK,
                onClick = {
                    // TODO: Implement Facebook Sign-In launcher
                    onFacebookSignIn()
                }
            )
            */
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // OR Divider
            Text(
                text = "OR",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Don't have an account? Sign up",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    ConnectHubTheme {
        LoginScreen()
    }
}