package com.connecthub.dataconnect

import kotlinx.serialization.Serializable
import java.util.UUID

// Message Data Types
@Serializable
data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String? = null,
    val content: String? = null,
    val messageType: MessageType,
    val mediaUrl: String? = null,
    val mediaFilename: String? = null,
    val mediaSize: Int? = null,
    val replyToId: String? = null,
    val isEdited: Boolean,
    val editedAt: String? = null,
    val createdAt: String,
    val sender: User? = null
)

// Query: GetConversationMessages
@Serializable
data class GetConversationMessagesVariables(
    val conversationId: String,
    val limit: Int = 50
)

@Serializable
data class GetConversationMessagesData(
    val messages: List<Message>
)

class GetConversationMessagesQuery {
    
    val operationName = "GetConversationMessages"
    
    suspend fun execute(conversationId: String, limit: Int = 50): GetConversationMessagesData {
        return GetConversationMessagesData(
            messages = listOf(
                Message(
                    id = UUID.randomUUID().toString(),
                    conversationId = conversationId,
                    senderId = "mock-sender-id",
                    content = "Hello! This is a test message from Data Connect.",
                    messageType = MessageType.TEXT,
                    isEdited = false,
                    createdAt = "2024-01-01T00:00:00Z",
                    sender = User(
                        id = "mock-sender-id",
                        firebaseUid = "mock-firebase-uid",
                        name = "Mock Sender",
                        status = UserStatus.ONLINE,
                        isAnonymous = false,
                        lastSeen = "2024-01-01T00:00:00Z",
                        createdAt = "2024-01-01T00:00:00Z",
                        updatedAt = "2024-01-01T00:00:00Z"
                    )
                )
            )
        )
    }
}

// Mutation: SendMessage
@Serializable
data class SendMessageVariables(
    val conversationId: String,
    val senderId: String,
    val content: String,
    val messageType: MessageType
)

@Serializable
data class SendMessageData(
    val message_insert: Message
)

class SendMessageMutation {
    
    val operationName = "SendMessage"
    
    suspend fun execute(
        conversationId: String,
        senderId: String,
        content: String,
        messageType: MessageType
    ): SendMessageData {
        return SendMessageData(
            message_insert = Message(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = senderId,
                content = content,
                messageType = messageType,
                isEdited = false,
                createdAt = "2024-01-01T00:00:00Z",
                sender = User(
                    id = senderId,
                    firebaseUid = "current-user-firebase-uid",
                    name = "Current User",
                    status = UserStatus.ONLINE,
                    isAnonymous = false,
                    lastSeen = "2024-01-01T00:00:00Z",
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
            )
        )
    }
}

// Mutation: SendMediaMessage
@Serializable
data class SendMediaMessageVariables(
    val conversationId: String,
    val senderId: String,
    val content: String? = null,
    val messageType: MessageType,
    val mediaUrl: String,
    val mediaFilename: String,
    val mediaSize: Int
)

@Serializable
data class SendMediaMessageData(
    val message_insert: Message
)

class SendMediaMessageMutation {
    
    val operationName = "SendMediaMessage"
    
    suspend fun execute(
        conversationId: String,
        senderId: String,
        content: String? = null,
        messageType: MessageType,
        mediaUrl: String,
        mediaFilename: String,
        mediaSize: Int
    ): SendMediaMessageData {
        return SendMediaMessageData(
            message_insert = Message(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = senderId,
                content = content,
                messageType = messageType,
                mediaUrl = mediaUrl,
                mediaFilename = mediaFilename,
                mediaSize = mediaSize,
                isEdited = false,
                createdAt = "2024-01-01T00:00:00Z",
                sender = User(
                    id = senderId,
                    firebaseUid = "current-user-firebase-uid",
                    name = "Current User",
                    status = UserStatus.ONLINE,
                    isAnonymous = false,
                    lastSeen = "2024-01-01T00:00:00Z",
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                )
            )
        )
    }
}