package com.connecthub.presentation.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.connecthub.presentation.auth.AuthViewModel
import com.connecthub.presentation.components.PrimaryButton
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.ForestGreen
import com.connecthub.presentation.theme.LightGray
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.White

@Composable
fun OTPScreen(
    onVerificationComplete: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onResendCode: () -> Unit = {},
    phoneNumber: String = "+1 234 567 8900",
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.uiState.collectAsState()
    var otpCode by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    
    // Auto-focus the input field when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Back button
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(32.dp)
                .background(
                    White.copy(alpha = 0.2f),
                    RoundedCornerShape(8.dp)
                )
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(18.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Phone icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(PrimaryGradient, CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Phone,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Title
        Text(
            text = "Verify Phone",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle
        Text(
            text = "Enter the code sent to $phoneNumber",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Error message
        authState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // OTP Input boxes (clickable to focus hidden input)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusRequester.requestFocus()
                },
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            repeat(6) { index ->
                OTPDigitBox(
                    digit = otpCode.getOrNull(index)?.toString() ?: "",
                    isFilled = index < otpCode.length,
                    isActive = index == otpCode.length, // Show cursor on next empty box
                    onClick = {
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Verify button
        PrimaryButton(
            text = if (authState.isLoading) "Verifying..." else "Verify",
            onClick = {
                if (otpCode.length == 6) {
                    viewModel.verifyPhoneCode(otpCode)
                    onVerificationComplete(otpCode)
                }
            },
            enabled = otpCode.length == 6 && !authState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Resend code
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Didn't receive code? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Resend",
                style = MaterialTheme.typography.bodyMedium,
                color = if (authState.isLoading) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(enabled = !authState.isLoading) { 
                    viewModel.resendPhoneCode(context as android.app.Activity)
                    onResendCode() 
                }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Hidden input field for keyboard capture - positioned off-screen but accessible
        BasicTextField(
            value = otpCode,
            onValueChange = { newValue -> 
                // Only allow digits and max 6 characters
                val digitsOnly = newValue.filter { it.isDigit() }
                if (digitsOnly.length <= 6) {
                    otpCode = digitsOnly
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Make it properly sized for keyboard interaction
                .focusRequester(focusRequester)
                .padding(horizontal = 16.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.01f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    // Almost transparent but allows keyboard interaction
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun OTPDigitBox(
    digit: String,
    isFilled: Boolean,
    isActive: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .border(
                width = 2.dp,
                color = when {
                    isActive -> MaterialTheme.colorScheme.primary
                    isFilled -> ForestGreen
                    else -> LightGray
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (digit.isNotEmpty()) {
            Text(
                text = digit,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else if (isActive) {
            // Show blinking cursor for active box
            Text(
                text = "|",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview
@Composable
fun OTPScreenPreview() {
    ConnectHubTheme {
        OTPScreen()
    }
}