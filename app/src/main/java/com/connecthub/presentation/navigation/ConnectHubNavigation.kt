package com.connecthub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.connecthub.presentation.auth.AuthViewModel
import com.connecthub.presentation.auth.SplashScreen
import com.connecthub.presentation.auth.login.LoginScreen
import com.connecthub.presentation.auth.otp.OTPScreen
import com.connecthub.presentation.auth.phone.PhoneAuthScreen
import com.connecthub.presentation.main.MainAppScreen

enum class Screen(val route: String) {
    SPLASH("splash"),
    LOGIN("login"),
    PHONE_AUTH("phone_auth"),
    OTP("otp"),
    DASHBOARD("dashboard")
}

@Composable
fun ConnectHubNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SPLASH.route,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    
    // Handle authentication state changes
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(Screen.DASHBOARD.route) {
                // Clear the entire backstack when authenticated
                popUpTo(0) { inclusive = true }
            }
        } else {
            // Handle logout - navigate back to login and clear backstack
            if (navController.currentDestination?.route == Screen.DASHBOARD.route) {
                navController.navigate(Screen.LOGIN.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.SPLASH.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.LOGIN.route) {
                        popUpTo(Screen.SPLASH.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.LOGIN.route) {
            LoginScreen(
                onNavigateToPhoneAuth = {
                    navController.navigate(Screen.PHONE_AUTH.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.DASHBOARD.route)
                },
                onGoogleSignIn = {
                    navController.navigate(Screen.DASHBOARD.route)
                },
                onFacebookSignIn = {
                    navController.navigate(Screen.DASHBOARD.route)
                }
            )
        }
        
        composable(Screen.PHONE_AUTH.route) {
            PhoneAuthScreen(
                onSendCode = { phoneNumber ->
                    navController.navigate(Screen.OTP.route)
                },
                onBackPressed = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }
        
        composable(Screen.OTP.route) {
            OTPScreen(
                onVerificationComplete = { code ->
                    // Navigation will be handled by AuthViewModel state changes
                },
                onBackPressed = {
                    navController.popBackStack()
                },
                onResendCode = {
                    // Handled by AuthViewModel
                },
                phoneNumber = authState.phoneNumber,
                viewModel = authViewModel
            )
        }
        
        composable(Screen.DASHBOARD.route) {
            MainAppScreen(authViewModel = authViewModel)
        }
    }
}