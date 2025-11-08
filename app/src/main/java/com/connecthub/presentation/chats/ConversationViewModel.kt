package com.connecthub.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connecthub.domain.repository.ConversationRepository
import com.connecthub.domain.model.Conversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ConversationUiState(
    val conversations: List<Conversation> = emptyList(),
    val filteredConversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentUserId: String? = null

    init {
        setupSearchFilter()
    }

    fun initialize(userId: String) {
        if (currentUserId != userId) {
            currentUserId = userId
            loadConversations(userId)
        }
    }

    private fun setupSearchFilter() {
        viewModelScope.launch {
            combine(_uiState, _searchQuery) { state, query ->
                val filtered = if (query.isBlank()) {
                    state.conversations
                } else {
                    state.conversations.filter { conversation ->
                        val displayName = when (conversation.type) {
                            com.connecthub.domain.model.ConversationType.GROUP -> conversation.name
                                ?: "Group Chat"

                            com.connecthub.domain.model.ConversationType.CHANNEL -> conversation.name
                                ?: "Channel"

                            com.connecthub.domain.model.ConversationType.DIRECT -> {
                                val otherParticipant =
                                    conversation.participants.firstOrNull { it != conversation.createdBy }
                                otherParticipant?.let { "User ${it.take(8)}" } ?: "Direct Chat"
                            }
                        }

                        // Search in display name, conversation name, and participant IDs
                        displayName.contains(query, ignoreCase = true) ||
                                (conversation.name?.contains(query, ignoreCase = true) == true) ||
                                conversation.participants.any { participantId ->
                                    participantId.contains(query, ignoreCase = true) ||
                                            "User ${participantId.take(8)}".contains(
                                                query,
                                                ignoreCase = true
                                            )
                                }
                    }
                }

                Timber.d("Search query: '$query', filtered ${filtered.size} out of ${state.conversations.size} conversations")
                Pair(state, filtered)
            }.collect { (state, filteredConversations) ->
                _uiState.value = state.copy(
                    filteredConversations = filteredConversations,
                    searchQuery = _searchQuery.value
                )
            }
        }
    }

    private fun loadConversations(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = conversationRepository.getUserConversations(userId)
                result.fold(
                    onSuccess = { conversations ->
                        val sortedConversations =
                            conversations.sortedByDescending { it.lastMessageTime }
                        _uiState.value = _uiState.value.copy(
                            conversations = sortedConversations,
                            filteredConversations = sortedConversations,
                            isLoading = false
                        )
                        Timber.d("Loaded ${conversations.size} conversations")
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                        )
                        Timber.e(error, "Failed to load conversations")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Timber.e(e, "Exception while loading conversations")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onConversationClick(conversation: Conversation) {
        // TODO: Navigate to chat conversation screen
        Timber.d("Clicked conversation: ${conversation.id}")
    }

    fun onCreateNewChat() {
        // TODO: Navigate to create new chat screen
        Timber.d("Create new chat clicked")
    }

    fun refreshConversations() {
        currentUserId?.let { userId ->
            loadConversations(userId)
        }
    }

    fun markConversationAsRead(conversationId: String) {
        _uiState.value = _uiState.value.copy(
            conversations = _uiState.value.conversations.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(unreadCount = 0)
                } else {
                    conversation
                }
            }
        )
    }
}