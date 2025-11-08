package com.connecthub.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connecthub.data.contacts.GoogleContactsSync
import com.connecthub.domain.model.Contact
import com.connecthub.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val filteredContacts: List<Contact> = emptyList(),
    val favoriteContacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showAddContactDialog: Boolean = false,
    val prefilledContactName: String? = null,
    val snackbarMessage: String? = null
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val googleContactsSync: GoogleContactsSync
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentUserId: String? = null

    init {
        setupSearchFilter()
    }

    fun initialize(userId: String) {
        if (currentUserId != userId) {
            currentUserId = userId
            loadContacts(userId)
        }
    }

    private fun setupSearchFilter() {
        viewModelScope.launch {
            combine(_uiState, _searchQuery) { state, query ->
                val filtered = if (query.isBlank()) {
                    state.contacts
                } else {
                    state.contacts.filter { contact ->
                        val displayName = contact.nickname ?: contact.contactUser?.displayName ?: ""
                        val email = contact.contactUser?.email ?: ""

                        displayName.contains(query, ignoreCase = true) ||
                                email.contains(query, ignoreCase = true)
                    }
                }

                Timber.d("Search query: '$query', filtered ${filtered.size} out of ${state.contacts.size} contacts")
                Pair(state, filtered)
            }.collect { (state, filteredContacts) ->
                _uiState.value = state.copy(
                    filteredContacts = filteredContacts,
                    searchQuery = _searchQuery.value
                )
            }
        }
    }

    private fun loadContacts(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = contactRepository.getUserContacts(userId)
                result.fold(
                    onSuccess = { contacts ->
                        // Sort contacts: favorites first, then by name
                        val sortedContacts = contacts.sortedWith(
                            compareByDescending<Contact> { it.isFavorite }
                                .thenBy { it.nickname ?: it.contactUser?.displayName ?: "" }
                        )

                        val favorites = contacts.filter { it.isFavorite }

                        _uiState.value = _uiState.value.copy(
                            contacts = sortedContacts,
                            filteredContacts = sortedContacts,
                            favoriteContacts = favorites,
                            isLoading = false
                        )
                        Timber.d("Loaded ${contacts.size} contacts (${favorites.size} favorites)")
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                        )
                        Timber.e(error, "Failed to load contacts")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Timber.e(e, "Exception while loading contacts")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onContactClick(contact: Contact) {
        // TODO: Navigate to contact details or start conversation
        Timber.d("Clicked contact: ${contact.id}")
    }

    fun onAddContact() {
        _uiState.value = _uiState.value.copy(
            showAddContactDialog = true,
            prefilledContactName = null
        )
    }

    fun onAddContactFromSearch(searchedName: String?) {
        _uiState.value = _uiState.value.copy(
            showAddContactDialog = true,
            prefilledContactName = searchedName
        )
    }

    fun onDismissAddContactDialog() {
        _uiState.value = _uiState.value.copy(
            showAddContactDialog = false,
            prefilledContactName = null
        )
    }

    fun addContact(name: String, email: String) {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                try {
                    val result = contactRepository.addContact(
                        userId = userId,
                        contactUserId = "user-${System.currentTimeMillis()}", // Placeholder
                        nickname = name
                    )

                    result.fold(
                        onSuccess = { newContact ->
                            Timber.d("Added contact: $name")
                            // Reload contacts to show the new one
                            loadContacts(userId)
                        },
                        onFailure = { error ->
                            _uiState.value = _uiState.value.copy(
                                error = "Failed to add contact: ${error.message}"
                            )
                            Timber.e(error, "Failed to add contact")
                        }
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "Error adding contact: ${e.message}"
                    )
                    Timber.e(e, "Exception while adding contact")
                }
            }
        }
    }

    fun onSyncGoogleContacts() {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                // Check permission first
                if (!googleContactsSync.hasContactsPermission()) {
                    showSnackbar("Contacts permission is required. Please grant permission in settings.")
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isSyncing = true, snackbarMessage = null)

                try {
                    // Fetch Google contacts
                    val result = googleContactsSync.fetchGoogleContacts()

                    result.fold(
                        onSuccess = { googleContacts ->
                            if (googleContacts.isEmpty()) {
                                _uiState.value = _uiState.value.copy(isSyncing = false)
                                showSnackbar("No contacts found on device")
                                return@fold
                            }

                            // Sync to repository
                            syncGoogleContacts(googleContacts)
                        },
                        onFailure = { error ->
                            _uiState.value = _uiState.value.copy(isSyncing = false)
                            showSnackbar("Failed to fetch contacts: ${error.message}")
                            Timber.e(error, "Failed to fetch Google contacts")
                        }
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    showSnackbar("Failed to sync contacts: ${e.message}")
                    Timber.e(e, "Exception while initiating sync")
                }
            }
        }
    }

    private fun syncGoogleContacts(contacts: List<Pair<String, String>>) {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                try {
                    var successCount = 0
                    var errorCount = 0

                    contacts.forEach { (name, email) ->
                        val result = contactRepository.addContact(
                            userId = userId,
                            contactUserId = "google-${email.hashCode()}",
                            nickname = name
                        )

                        result.fold(
                            onSuccess = { successCount++ },
                            onFailure = { errorCount++ }
                        )
                    }

                    Timber.d("Synced $successCount contacts, $errorCount errors")

                    // Reload all contacts
                    loadContacts(userId)

                    _uiState.value = _uiState.value.copy(isSyncing = false)

                    // Show success message
                    showSnackbar("Successfully synced $successCount contacts!")
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isSyncing = false)
                    showSnackbar("Failed to sync contacts: ${e.message}")
                    Timber.e(e, "Exception while syncing contacts")
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    fun onToggleFavorite(contact: Contact) {
        viewModelScope.launch {
            try {
                val result = contactRepository.toggleFavorite(contact.id, !contact.isFavorite)
                result.fold(
                    onSuccess = {
                        // Update local state immediately
                        val updatedContacts = _uiState.value.contacts.map { c ->
                            if (c.id == contact.id) {
                                c.copy(isFavorite = !c.isFavorite)
                            } else {
                                c
                            }
                        }

                        // Re-sort: favorites first, then alphabetically
                        val sortedContacts = updatedContacts.sortedWith(
                            compareByDescending<Contact> { it.isFavorite }
                                .thenBy { it.nickname ?: it.contactUser?.displayName ?: "" }
                        )

                        // Update favorites list
                        val favorites = sortedContacts.filter { it.isFavorite }

                        _uiState.value = _uiState.value.copy(
                            contacts = sortedContacts,
                            filteredContacts = sortedContacts,
                            favoriteContacts = favorites
                        )

                        Timber.d("Toggled favorite for contact: ${contact.id}, now ${favorites.size} favorites")
                    },
                    onFailure = { error ->
                        Timber.e(error, "Failed to toggle favorite")
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Exception while toggling favorite")
            }
        }
    }

    fun refreshContacts() {
        currentUserId?.let { userId ->
            loadContacts(userId)
        }
    }

    fun getContactsBySection(): Map<String, List<Contact>> {
        val contacts = _uiState.value.filteredContacts

        // Group contacts by first letter of display name
        return contacts.groupBy { contact ->
            val displayName = contact.nickname ?: contact.contactUser?.displayName ?: "#"
            displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "#"
        }.toSortedMap()
    }
}