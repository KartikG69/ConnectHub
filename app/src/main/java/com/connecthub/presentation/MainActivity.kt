package com.connecthub.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.connecthub.presentation.navigation.ConnectHubNavigation
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.utils.constants.FirebaseConstants
import com.connecthub.utils.testing.TestAuthHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        installSplashScreen()
        enableEdgeToEdge()
        
        // Show test instructions if test mode is enabled
        if (FirebaseConstants.ENABLE_TEST_MODE) {
            TestAuthHelper.logTestInstructions()
        }
        
        setContent {
            ConnectHubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConnectHubApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConnectHubApp(modifier: Modifier = Modifier) {
    ConnectHubNavigation(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun ConnectHubAppPreview() {
    ConnectHubTheme {
        ConnectHubApp()
    }
}