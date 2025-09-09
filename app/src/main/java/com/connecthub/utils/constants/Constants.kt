package com.connecthub.utils.constants

object Constants {
    
    // Firebase Collections
    const val USERS_COLLECTION = "users"
    const val CALLS_COLLECTION = "calls"
    const val MESSAGES_COLLECTION = "messages"
    const val CONVERSATIONS_COLLECTION = "conversations"
    
    // WebRTC
    const val LOCAL_TRACK_ID = "local_track"
    const val LOCAL_STREAM_ID = "local_stream"
    
    // Shared Preferences
    const val PREFS_NAME = "ConnectHubPrefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_EMAIL = "user_email"
    
    // Intent Extras
    const val EXTRA_CALL_ID = "call_id"
    const val EXTRA_CALL_TYPE = "call_type"
    const val EXTRA_PARTICIPANT_ID = "participant_id"
    
    // Call Types
    const val CALL_TYPE_VIDEO = "video"
    const val CALL_TYPE_AUDIO = "audio"
    
    // Permissions
    val CAMERA_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )
}