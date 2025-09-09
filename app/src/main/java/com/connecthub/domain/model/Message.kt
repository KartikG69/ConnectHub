package com.connecthub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val senderPhotoUrl: String? = null,
    val content: String,
    val timestamp: Long,
    val type: MessageType = MessageType.TEXT,
    val isRead: Boolean = false,
    val readBy: List<String> = emptyList(),
    val attachments: List<MessageAttachment> = emptyList(),
    val replyToMessageId: String? = null,
    val isEdited: Boolean = false,
    val editedAt: Long? = null
)

@Serializable
enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE,
    LOCATION,
    CONTACT
}

@Serializable
data class MessageAttachment(
    val id: String,
    val name: String,
    val url: String,
    val type: String,
    val size: Long,
    val thumbnailUrl: String? = null
)