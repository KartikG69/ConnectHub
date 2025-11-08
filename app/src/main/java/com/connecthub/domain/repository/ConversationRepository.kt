package com.connecthub.domain.repository

import com.connecthub.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun getUserConversations(userId: String): Result<List<Conversation>>
    suspend fun getConversationById(conversationId: String): Result<Conversation?>
    suspend fun createDirectConversation(userId1: String, userId2: String): Result<Conversation>
    suspend fun createGroupConversation(
        name: String,
        creatorId: String,
        participantIds: List<String>
    ): Result<Conversation>

    suspend fun addParticipantToConversation(conversationId: String, userId: String): Result<Unit>
    suspend fun removeParticipantFromConversation(
        conversationId: String,
        userId: String
    ): Result<Unit>

    fun observeConversationUpdates(userId: String): Flow<List<Conversation>>
}