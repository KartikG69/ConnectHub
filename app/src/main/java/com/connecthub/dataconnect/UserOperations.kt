package com.connecthub.dataconnect

import kotlinx.serialization.Serializable
import java.util.UUID

// User Data Types
@Serializable
data class User(
    val id: String,
    val firebaseUid: String,
    val name: String,
    val email: String? = null,
    val profileImageUrl: String? = null,
    val status: UserStatus,
    val isAnonymous: Boolean,
    val lastSeen: String, // ISO timestamp
    val createdAt: String,
    val updatedAt: String
)

// Query: GetUserByFirebaseUid
@Serializable
data class GetUserByFirebaseUidVariables(
    val firebaseUid: String
)

@Serializable
data class GetUserByFirebaseUidData(
    val user: User?
)

class GetUserByFirebaseUidQuery {
    
    val operationName = "GetUserByFirebaseUid"
    
    suspend fun execute(firebaseUid: String): GetUserByFirebaseUidData {
        // This would be auto-generated to call Firebase Data Connect
        // For now, return mock data
        return GetUserByFirebaseUidData(
            user = User(
                id = UUID.randomUUID().toString(),
                firebaseUid = firebaseUid,
                name = "Mock User",
                email = "mock@example.com",
                status = UserStatus.OFFLINE,
                isAnonymous = false,
                lastSeen = "2024-01-01T00:00:00Z",
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}

// Mutation: CreateUser
@Serializable
data class CreateUserVariables(
    val firebaseUid: String,
    val name: String,
    val email: String? = null,
    val isAnonymous: Boolean
)

@Serializable
data class CreateUserData(
    val user_insert: User
)

class CreateUserMutation {
    
    val operationName = "CreateUser"
    
    suspend fun execute(
        firebaseUid: String,
        name: String,
        email: String? = null,
        isAnonymous: Boolean
    ): CreateUserData {
        return CreateUserData(
            user_insert = User(
                id = UUID.randomUUID().toString(),
                firebaseUid = firebaseUid,
                name = name,
                email = email,
                status = UserStatus.OFFLINE,
                isAnonymous = isAnonymous,
                lastSeen = "2024-01-01T00:00:00Z",
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}

// Mutation: UpdateUserStatus
@Serializable
data class UpdateUserStatusVariables(
    val id: String,
    val status: UserStatus,
    val lastSeen: String
)

@Serializable
data class UpdateUserStatusData(
    val user_update: User
)

class UpdateUserStatusMutation {
    
    val operationName = "UpdateUserStatus"
    
    suspend fun execute(
        id: String,
        status: UserStatus,
        lastSeen: String = "2024-01-01T00:00:00Z"
    ): UpdateUserStatusData {
        return UpdateUserStatusData(
            user_update = User(
                id = id,
                firebaseUid = "mock-firebase-uid",
                name = "Updated User",
                status = status,
                isAnonymous = false,
                lastSeen = lastSeen,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
    }
}

// Query: SearchUsers
@Serializable
data class SearchUsersVariables(
    val query: String
)

@Serializable
data class SearchUsersData(
    val users: List<User>
)

class SearchUsersQuery {
    
    val operationName = "SearchUsers"
    
    suspend fun execute(query: String): SearchUsersData {
        return SearchUsersData(
            users = listOf(
                User(
                    id = UUID.randomUUID().toString(),
                    firebaseUid = "search-result-1",
                    name = "Search Result",
                    email = "search@example.com",
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