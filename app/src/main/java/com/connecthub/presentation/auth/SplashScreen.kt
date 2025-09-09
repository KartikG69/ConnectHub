package com.connecthub.presentation.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.ForestGreen
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit = {}
) {
    var animationStarted by remember { mutableStateOf(false) }
    
    val offsetY by animateFloatAsState(
        targetValue = if (animationStarted) -10f else 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000)
        ),
        label = "logo_animation"
    )
    
    LaunchedEffect(Unit) {
        animationStarted = true
        delay(3000)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = offsetY.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CH",
                    color = ForestGreen,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Name
            Text(
                text = "ConnectHub",
                color = White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline
            Text(
                text = "Connect. Call. Collaborate.",
                color = White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
        
        // Decorative circles
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 100.dp, y = (-150).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            White.copy(alpha = 0.05f),
                            androidx.compose.ui.graphics.Color.Transparent
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        )
        
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-80).dp, y = 120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            White.copy(alpha = 0.05f),
                            androidx.compose.ui.graphics.Color.Transparent
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    ConnectHubTheme {
        SplashScreen()
    }
}