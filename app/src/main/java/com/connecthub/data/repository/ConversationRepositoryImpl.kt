package com.connecthub.data.repository

import com.connecthub.data.remote.dataconnect.ConversationDataSource
import com.connecthub.domain.model.Conversation
import com.connecthub.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor(
    private val conversationDataSource: ConversationDataSource
) : ConversationRepository {

    override suspend fun getUserConversations(userId: String): Result<List<Conversation>> {
        return conversationDataSource.getUserConversations(userId)
    }

    override suspend fun getConversationById(conversationId: String): Result<Conversation?> {
        return conversationDataSource.getConversationById(conversationId)
    }

    override suspend fun createDirectConversation(
        userId1: String,
        userId2: String
    ): Result<Conversation> {
        return conversationDataSource.createDirectConversation(userId1, userId2)
    }

    override suspend fun createGroupConversation(
        name: String,
        creatorId: String,
        participantIds: List<String>
    ): Result<Conversation> {
        return conversationDataSource.createGroupConversation(name, creatorId, participantIds)
    }

    override suspend fun addParticipantToConversation(
        conversationId: String,
        userId: String
    ): Result<Unit> {
        return conversationDataSource.addParticipantToConversation(conversationId, userId)
    }

    override suspend fun removeParticipantFromConversation(
        conversationId: String,
        userId: String
    ): Result<Unit> {
        return conversationDataSource.removeParticipantFromConversation(conversationId, userId)
    }

    override fun observeConversationUpdates(userId: String): Flow<List<Conversation>> {
        return conversationDataSource.observeConversationUpdates(userId)
    }
}