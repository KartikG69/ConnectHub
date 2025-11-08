package com.connecthub.data.repository

import com.connecthub.data.remote.dataconnect.ContactDataSource
import com.connecthub.domain.model.Contact
import com.connecthub.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource
) : ContactRepository {

    override suspend fun getUserContacts(userId: String): Result<List<Contact>> {
        return contactDataSource.getUserContacts(userId)
    }

    override suspend fun getContactById(contactId: String): Result<Contact?> {
        return contactDataSource.getContactById(contactId)
    }

    override suspend fun addContact(
        userId: String,
        contactUserId: String,
        nickname: String?
    ): Result<Contact> {
        return contactDataSource.addContact(userId, contactUserId, nickname)
    }

    override suspend fun removeContact(contactId: String): Result<Unit> {
        return contactDataSource.removeContact(contactId)
    }

    override suspend fun updateContact(contact: Contact): Result<Unit> {
        return contactDataSource.updateContact(contact)
    }

    override suspend fun toggleFavorite(contactId: String, isFavorite: Boolean): Result<Unit> {
        return contactDataSource.toggleFavorite(contactId, isFavorite)
    }

    override suspend fun toggleBlock(contactId: String, isBlocked: Boolean): Result<Unit> {
        return contactDataSource.toggleBlock(contactId, isBlocked)
    }

    override suspend fun searchContacts(userId: String, query: String): Result<List<Contact>> {
        return contactDataSource.searchContacts(userId, query)
    }

    override fun observeContactUpdates(userId: String): Flow<List<Contact>> {
        return contactDataSource.observeContactUpdates(userId)
    }
}