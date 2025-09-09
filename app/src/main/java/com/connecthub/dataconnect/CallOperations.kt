package com.connecthub.dataconnect

import kotlinx.serialization.Serializable
import java.util.UUID

// Call Data Types
@Serializable
data class Call(
    val id: String,
    val callerId: String? = null,
    val receiverId: String? = null,
    val conversationId: String? = null,
    val callType: CallType,
    val status: CallStatus,
    val durationSeconds: Int,
    val startedAt: String,
    val endedAt: String? = null,
    val createdAt: String,
    val caller: User? = null,
    val receiver: User? = null
)

// Query: GetUserCalls
@Serializable
data class GetUserCallsVariables(
    val userId: String
)

@Serializable
data class GetUserCallsData(
    val calls: List<Call>
)

class GetUserCallsQuery {
    
    val operationName = "GetUserCalls"
    
    suspend fun execute(userId: String): GetUserCallsData {
        return GetUserCallsData(
            calls = listOf(
                Call(
                    id = UUID.randomUUID().toString(),
                    callerId = userId,
                    receiverId = "other-user-id",
                    callType = CallType.VIDEO,
                    status = CallStatus.ENDED,
                    durationSeconds = 125, // 2 minutes 5 seconds
                    startedAt = "2024-01-01T00:00:00Z",
                    endedAt = "2024-01-01T00:02:05Z",
                    createdAt = "2024-01-01T00:00:00Z",
                    caller = User(
                        id = userId,
                        firebaseUid = "current-user-firebase-uid",
                        name = "Current User",
                        status = UserStatus.ONLINE,
                        isAnonymous = false,
                        lastSeen = "2024-01-01T00:00:00Z",
                        createdAt = "2024-01-01T00:00:00Z",
                        updatedAt = "2024-01-01T00:00:00Z"
                    ),
                    receiver = User(
                        id = "other-user-id",
                        firebaseUid = "other-firebase-uid",
                        name = "Other User",
                        status = UserStatus.OFFLINE,
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

// Mutation: CreateCall
@Serializable
data class CreateCallVariables(
    val callerId: String,
    val receiverId: String,
    val callType: CallType,
    val conversationId: String? = null
)

@Serializable
data class CreateCallData(
    val call_insert: Call
)

class CreateCallMutation {
    
    val operationName = "CreateCall"
    
    suspend fun execute(
        callerId: String,
        receiverId: String,
        callType: CallType,
        conversationId: String? = null
    ): CreateCallData {
        return CreateCallData(
            call_insert = Call(
                id = UUID.randomUUID().toString(),
                callerId = callerId,
                receiverId = receiverId,
                conversationId = conversationId,
                callType = callType,
                status = CallStatus.INCOMING,
                durationSeconds = 0,
                startedAt = "2024-01-01T00:00:00Z",
                createdAt = "2024-01-01T00:00:00Z",
                caller = User(
                    id = callerId,
                    firebaseUid = "caller-firebase-uid",
                    name = "Caller",
                    status = UserStatus.ONLINE,
                    isAnonymous = false,
                    lastSeen = "2024-01-01T00:00:00Z",
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                ),
                receiver = User(
                    id = receiverId,
                    firebaseUid = "receiver-firebase-uid",
                    name = "Receiver",
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

// Mutation: UpdateCallStatus
@Serializable
data class UpdateCallStatusVariables(
    val callId: String,
    val status: CallStatus,
    val endedAt: String? = null,
    val durationSeconds: Int? = null
)

@Serializable
data class UpdateCallStatusData(
    val call_update: Call
)

class UpdateCallStatusMutation {
    
    val operationName = "UpdateCallStatus"
    
    suspend fun execute(
        callId: String,
        status: CallStatus,
        endedAt: String? = null,
        durationSeconds: Int? = null
    ): UpdateCallStatusData {
        return UpdateCallStatusData(
            call_update = Call(
                id = callId,
                callType = CallType.VIDEO,
                status = status,
                durationSeconds = durationSeconds ?: 0,
                startedAt = "2024-01-01T00:00:00Z",
                endedAt = endedAt,
                createdAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}