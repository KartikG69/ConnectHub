package com.connecthub.presentation.auth.phone

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connecthub.presentation.auth.AuthViewModel
import com.connecthub.presentation.components.InputField
import com.connecthub.presentation.components.PrimaryButton
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.White

data class CountryCode(
    val name: String,
    val code: String,
    val dialCode: String
)

val countryCodesList = listOf(
    CountryCode("United States", "US", "+1"),
    CountryCode("United Kingdom", "GB", "+44"),
    CountryCode("India", "IN", "+91"),
    CountryCode("Canada", "CA", "+1"),
    CountryCode("Australia", "AU", "+61"),
    CountryCode("Germany", "DE", "+49"),
    CountryCode("France", "FR", "+33"),
    CountryCode("Japan", "JP", "+81"),
    CountryCode("South Korea", "KR", "+82"),
    CountryCode("China", "CN", "+86")
)

fun formatPhoneNumber(number: String, countryCode: String): String {
    val digits = number.filter { it.isDigit() }
    return when (countryCode) {
        "+1" -> { // US/Canada format: (XXX) XXX-XXXX
            when (digits.length) {
                in 0..3 -> digits
                in 4..6 -> "(${digits.substring(0, 3)}) ${digits.substring(3)}"
                in 7..10 -> "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6)}"
                else -> "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6, 10)}"
            }
        }
        "+91" -> { // India format: XXXXX-XXXXX
            when (digits.length) {
                in 0..5 -> digits
                in 6..10 -> "${digits.substring(0, 5)}-${digits.substring(5)}"
                else -> "${digits.substring(0, 5)}-${digits.substring(5, 10)}"
            }
        }
        "+44" -> { // UK format: XXXXX XXXXXX
            when (digits.length) {
                in 0..5 -> digits
                in 6..11 -> "${digits.substring(0, 5)} ${digits.substring(5)}"
                else -> "${digits.substring(0, 5)} ${digits.substring(5, 11)}"
            }
        }
        else -> { // Generic format: XXX XXX XXXX
            when (digits.length) {
                in 0..3 -> digits
                in 4..6 -> "${digits.substring(0, 3)} ${digits.substring(3)}"
                in 7..10 -> "${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
                else -> digits.take(10)
            }
        }
    }
}

fun getMaxDigits(countryCode: String): Int {
    return when (countryCode) {
        "+1" -> 10 // US/Canada
        "+91" -> 10 // India
        "+44" -> 11 // UK
        "+49" -> 11 // Germany
        "+33" -> 10 // France
        "+81" -> 11 // Japan
        "+82" -> 11 // South Korea
        "+86" -> 11 // China
        "+61" -> 10 // Australia
        else -> 10
    }
}

fun isValidPhoneNumber(phoneNumber: String, countryCode: String): Boolean {
    val digits = phoneNumber.filter { it.isDigit() }
    val maxDigits = getMaxDigits(countryCode)
    return digits.length >= maxDigits
}

@Composable
fun PhoneAuthScreen(
    onSendCode: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val authState by viewModel.uiState.collectAsState()
    
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf(countryCodesList.find { it.dialCode == "+91" } ?: countryCodesList[0]) }
    var showCountryDropdown by remember { mutableStateOf(false) }
    
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
            text = "Enter Phone Number",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle
        Text(
            text = "We'll send you a verification code",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Country code selector and phone input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Country code selector
            Box {
                Row(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showCountryDropdown = true }
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCountry.dialCode,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select country",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                DropdownMenu(
                    expanded = showCountryDropdown,
                    onDismissRequest = { showCountryDropdown = false },
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(12.dp)
                    )
                ) {
                    countryCodesList.forEach { country ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = country.dialCode,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.width(50.dp)
                                    )
                                    Text(
                                        text = country.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            onClick = {
                                selectedCountry = country
                                showCountryDropdown = false
                                // Clear phone number when changing country
                                phoneNumber = ""
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Phone input
            InputField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    // Only allow digits, limit by country max
                    val digits = newValue.filter { it.isDigit() }
                    val maxDigits = getMaxDigits(selectedCountry.dialCode)
                    
                    if (digits.length <= maxDigits) {
                        // Store raw digits while typing for smooth experience
                        phoneNumber = digits
                    }
                },
                placeholder = "Enter phone number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
        
        // Send code button
        PrimaryButton(
            text = if (authState.isLoading) "Sending..." else "Send Code",
            onClick = {
                val fullNumber = "${selectedCountry.dialCode}${phoneNumber.filter { it.isDigit() }}"
                if (isValidPhoneNumber(phoneNumber, selectedCountry.dialCode)) {
                    viewModel.sendPhoneVerificationCode(fullNumber, context as android.app.Activity)
                    onSendCode(fullNumber)
                }
            },
            enabled = isValidPhoneNumber(phoneNumber, selectedCountry.dialCode) && !authState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Terms text
        Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun PhoneAuthScreenPreview() {
    ConnectHubTheme {
        PhoneAuthScreen()
    }
}