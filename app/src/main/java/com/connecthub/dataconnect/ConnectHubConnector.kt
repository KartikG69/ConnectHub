package com.connecthub.dataconnect

import kotlinx.serialization.Serializable

/**
 * Generated Firebase Data Connect SDK for ConnectHub
 * This simulates the auto-generated connector that would be created by Firebase CLI
 */
object ConnectHubConnector {
    
    val instance: ConnectHubConnectorInterface by lazy {
        ConnectHubConnectorImpl()
    }
}

interface ConnectHubConnectorInterface {
    
    // User operations
    val getUserByFirebaseUid: GetUserByFirebaseUidQuery
    val createUser: CreateUserMutation
    val updateUserStatus: UpdateUserStatusMutation
    val searchUsers: SearchUsersQuery
    
    // Message operations  
    val getConversationMessages: GetConversationMessagesQuery
    val sendMessage: SendMessageMutation
    val sendMediaMessage: SendMediaMessageMutation
    
    // Conversation operations
    val getUserConversations: GetUserConversationsQuery
    val createDirectConversation: CreateDirectConversationMutation
    val createGroupConversation: CreateGroupConversationMutation
    
    // Call operations
    val getUserCalls: GetUserCallsQuery
    val createCall: CreateCallMutation
    val updateCallStatus: UpdateCallStatusMutation
}

internal class ConnectHubConnectorImpl : ConnectHubConnectorInterface {
    
    override val getUserByFirebaseUid = GetUserByFirebaseUidQuery()
    override val createUser = CreateUserMutation()
    override val updateUserStatus = UpdateUserStatusMutation()
    override val searchUsers = SearchUsersQuery()
    
    override val getConversationMessages = GetConversationMessagesQuery()
    override val sendMessage = SendMessageMutation()
    override val sendMediaMessage = SendMediaMessageMutation()
    
    override val getUserConversations = GetUserConversationsQuery()
    override val createDirectConversation = CreateDirectConversationMutation()
    override val createGroupConversation = CreateGroupConversationMutation()
    
    override val getUserCalls = GetUserCallsQuery()
    override val createCall = CreateCallMutation()
    override val updateCallStatus = UpdateCallStatusMutation()
}

// Enums matching GraphQL schema
@Serializable
enum class UserStatus { ONLINE, OFFLINE, AWAY, BUSY }

@Serializable
enum class MessageType { TEXT, IMAGE, AUDIO, VIDEO, FILE }

@Serializable
enum class CallType { VIDEO, AUDIO }

@Serializable
enum class CallStatus { INCOMING, OUTGOING, MISSED, ANSWERED, DECLINED, ENDED }