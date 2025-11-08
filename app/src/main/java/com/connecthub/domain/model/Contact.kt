package com.connecthub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String, // Contact relationship ID
    val userId: String, // The user who owns this contact list
    val contactUserId: String, // The actual contact (user ID)
    val contactUser: User? = null, // Populated contact user details
    val nickname: String? = null, // Optional custom nickname
    val isFavorite: Boolean = false,
    val isBlocked: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
    val lastInteractionAt: Long = System.currentTimeMillis()
)

enum class ContactStatus {
    ONLINE,
    OFFLINE,
    AWAY,
    DO_NOT_DISTURB
}