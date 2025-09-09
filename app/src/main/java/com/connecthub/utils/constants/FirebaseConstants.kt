package com.connecthub.utils.constants

object FirebaseConstants {
    // TODO: Replace with ACTUAL Web Client ID from Firebase Console
    // This should look like: "123456789-abcdefghijklmnop.apps.googleusercontent.com"
    // NOT an API key! Get it from: Firebase Console → Project Settings → General → Web client ID
    const val WEB_CLIENT_ID = "546165808864-99bi49m6oapd1lemeh5naf1utdthaptp.apps.googleusercontent.com"
    
    // Test mode configuration
    const val ENABLE_TEST_MODE = true // Set to false for production
    
    // Test phone numbers (these won't send real SMS)
    const val TEST_PHONE_NUMBER = "+917037644920"
    const val TEST_VERIFICATION_CODE = "123456"
    
    // Alternative test numbers
    const val TEST_PHONE_US = "+15551234567"  
    const val TEST_PHONE_INDIA = "+917037644920"
    const val TEST_PHONE_UK = "+447123456789"
    
    // Test Google account (for testing purposes)
    const val TEST_GOOGLE_EMAIL = "test@connecthub.com"
    const val TEST_GOOGLE_NAME = "Test User"
}
