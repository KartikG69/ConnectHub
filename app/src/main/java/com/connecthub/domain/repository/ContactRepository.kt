package com.connecthub.domain.repository

import com.connecthub.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    /**
     * Get all contacts for a user
     */
    suspend fun getUserContacts(userId: String): Result<List<Contact>>

    /**
     * Get a specific contact by ID
     */
    suspend fun getContactById(contactId: String): Result<Contact?>

    /**
     * Add a new contact
     */
    suspend fun addContact(
        userId: String,
        contactUserId: String,
        nickname: String? = null
    ): Result<Contact>

    /**
     * Remove a contact
     */
    suspend fun removeContact(contactId: String): Result<Unit>

    /**
     * Update contact details (nickname, favorite status, etc.)
     */
    suspend fun updateContact(contact: Contact): Result<Unit>

    /**
     * Mark contact as favorite
     */
    suspend fun toggleFavorite(contactId: String, isFavorite: Boolean): Result<Unit>

    /**
     * Block/unblock a contact
     */
    suspend fun toggleBlock(contactId: String, isBlocked: Boolean): Result<Unit>

    /**
     * Search contacts by name or email
     */
    suspend fun searchContacts(userId: String, query: String): Result<List<Contact>>

    /**
     * Observe real-time contact updates
     */
    fun observeContactUpdates(userId: String): Flow<List<Contact>>
}