package com.connecthub.utils.testing

import com.connecthub.utils.constants.FirebaseConstants
import timber.log.Timber

/**
 * Test helper for authentication flows
 * Only works when ENABLE_TEST_MODE = true
 */
object TestAuthHelper {
    
    fun logTestInstructions() {
        if (!FirebaseConstants.ENABLE_TEST_MODE) return
        
        Timber.d("🧪 ==================== TEST MODE ACTIVE ====================")
        Timber.d("📱 PHONE AUTH TESTING:")
        Timber.d("   1. Use phone: ${FirebaseConstants.TEST_PHONE_NUMBER}")
        Timber.d("   2. Use any other real phone number for real SMS")
        Timber.d("   3. For test number, use code: ${FirebaseConstants.TEST_VERIFICATION_CODE}")
        Timber.d("")
        Timber.d("🔍 GOOGLE SIGN-IN TESTING:")
        Timber.d("   1. Ensure WEB_CLIENT_ID is configured correctly")
        Timber.d("   2. Check Firebase Console for SHA-1 fingerprint")
        Timber.d("   3. Use any Google account to sign in")
        Timber.d("")
        Timber.d("🔧 DEBUGGING TIPS:")
        Timber.d("   - Watch Logcat for detailed logs with emojis")
        Timber.d("   - All auth steps are logged with 📱🔐🎉❌ emojis")
        Timber.d("   - Check 'ConnectHub' and 'FirebaseAuth' tags in Logcat")
        Timber.d("🧪 ========================================================")
    }
    
    fun isTestPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber in listOf(
            FirebaseConstants.TEST_PHONE_NUMBER,
            FirebaseConstants.TEST_PHONE_US,
            FirebaseConstants.TEST_PHONE_INDIA,
            FirebaseConstants.TEST_PHONE_UK
        )
    }
    
    fun getTestInstructions(): String {
        return if (FirebaseConstants.ENABLE_TEST_MODE) {
            """
            🧪 TEST MODE ACTIVE
            
            Phone Testing:
            • Use ${FirebaseConstants.TEST_PHONE_NUMBER} with code ${FirebaseConstants.TEST_VERIFICATION_CODE}
            • Or use any real number for actual SMS
            
            Google Sign-In:
            • Make sure WEB_CLIENT_ID is configured
            • Use any Google account
            
            Check Logcat for detailed logs!
            """.trimIndent()
        } else {
            "Production mode - no test helpers available"
        }
    }
}