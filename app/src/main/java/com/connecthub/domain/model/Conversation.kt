package com.connecthub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val name: String? = null,
    val type: ConversationType,
    val participants: List<String>,
    val participantDetails: Map<String, User> = emptyMap(),
    val lastMessage: Message? = null,
    val lastMessageTime: Long = 0L,
    val createdAt: Long,
    val createdBy: String,
    val isArchived: Boolean = false,
    val isMuted: Boolean = false,
    val unreadCount: Int = 0,
    val photoUrl: String? = null,
    val description: String? = null
)

@Serializable
enum class ConversationType {
    DIRECT,
    GROUP,
    CHANNEL
}