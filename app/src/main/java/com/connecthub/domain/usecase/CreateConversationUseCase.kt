package com.connecthub.domain.usecase

import com.connecthub.domain.model.Conversation
import com.connecthub.domain.repository.ConversationRepository
import javax.inject.Inject

class CreateConversationUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun createDirectConversation(userId1: String, userId2: String): Result<Conversation> {
        return conversationRepository.createDirectConversation(userId1, userId2)
    }

    suspend fun createGroupConversation(
        name: String,
        creatorId: String,
        participantIds: List<String>
    ): Result<Conversation> {
        return conversationRepository.createGroupConversation(name, creatorId, participantIds)
    }
}