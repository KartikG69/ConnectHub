package com.connecthub.domain.usecase

import com.connecthub.domain.model.Contact
import com.connecthub.domain.repository.ContactRepository
import javax.inject.Inject

class GetUserContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Contact>> {
        return contactRepository.getUserContacts(userId)
    }
}