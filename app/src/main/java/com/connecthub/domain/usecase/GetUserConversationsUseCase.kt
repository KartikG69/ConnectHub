package com.connecthub.domain.usecase

import com.connecthub.domain.model.Conversation
import com.connecthub.domain.repository.ConversationRepository
import javax.inject.Inject

class GetUserConversationsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Conversation>> {
        return conversationRepository.getUserConversations(userId)
    }
}