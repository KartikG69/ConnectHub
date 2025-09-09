package com.connecthub.dataconnect

import kotlinx.serialization.Serializable
import java.util.UUID

// Conversation Data Types
@Serializable
data class Conversation(
    val id: String,
    val name: String? = null,
    val isGroup: Boolean,
    val lastMessageAt: String? = null,
    val updatedAt: String,
    val participants: List<User> = emptyList(),
    val lastMessage: Message? = null
)

// Query: GetUserConversations
@Serializable
data class GetUserConversationsVariables(
    val userId: String
)

@Serializable
data class GetUserConversationsData(
    val conversations: List<Conversation>
)

class GetUserConversationsQuery {
    
    val operationName = "GetUserConversations"
    
    suspend fun execute(userId: String): GetUserConversationsData {
        return GetUserConversationsData(
            conversations = listOf(
                Conversation(
                    id = UUID.randomUUID().toString(),
                    name = null, // Direct conversation
                    isGroup = false,
                    lastMessageAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z",
                    participants = listOf(
                        User(
                            id = "other-user-id",
                            firebaseUid = "other-firebase-uid",
                            name = "Other User",
                            status = UserStatus.ONLINE,
                            isAnonymous = false,
                            lastSeen = "2024-01-01T00:00:00Z",
                            createdAt = "2024-01-01T00:00:00Z",
                            updatedAt = "2024-01-01T00:00:00Z"
                        )
                    ),
                    lastMessage = Message(
                        id = "last-message-id",
                        conversationId = "conversation-id",
                        senderId = "other-user-id",
                        content = "Hey there!",
                        messageType = MessageType.TEXT,
                        isEdited = false,
                        createdAt = "2024-01-01T00:00:00Z",
                        sender = User(
                            id = "other-user-id",
                            firebaseUid = "other-firebase-uid",
                            name = "Other User",
                            status = UserStatus.ONLINE,
                            isAnonymous = false,
                            lastSeen = "2024-01-01T00:00:00Z",
                            createdAt = "2024-01-01T00:00:00Z",
                            updatedAt = "2024-01-01T00:00:00Z"
                        )
                    )
                )
            )
        )
    }
}

// Mutation: CreateDirectConversation
@Serializable
data class CreateDirectConversationVariables(
    val userId1: String,
    val userId2: String
)

@Serializable
data class CreateDirectConversationData(
    val conversation_insert: Conversation
)

class CreateDirectConversationMutation {
    
    val operationName = "CreateDirectConversation"
    
    suspend fun execute(userId1: String, userId2: String): CreateDirectConversationData {
        return CreateDirectConversationData(
            conversation_insert = Conversation(
                id = UUID.randomUUID().toString(),
                name = null,
                isGroup = false,
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}

// Mutation: CreateGroupConversation
@Serializable
data class CreateGroupConversationVariables(
    val name: String,
    val creatorId: String
)

@Serializable
data class CreateGroupConversationData(
    val conversation_insert: Conversation
)

class CreateGroupConversationMutation {
    
    val operationName = "CreateGroupConversation"
    
    suspend fun execute(name: String, creatorId: String): CreateGroupConversationData {
        return CreateGroupConversationData(
            conversation_insert = Conversation(
                id = UUID.randomUUID().toString(),
                name = name,
                isGroup = true,
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}