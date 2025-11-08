package com.connecthub.data.contacts

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleContactsSync @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Fetch contacts from the device's Google account
     * Returns a list of Pair<Name, Email>
     */
    suspend fun fetchGoogleContacts(): Result<List<Pair<String, String>>> =
        withContext(Dispatchers.IO) {
            try {
                val contacts = mutableListOf<Pair<String, String>>()

                val cursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.ADDRESS
                    ),
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Email.DISPLAY_NAME + " ASC"
                )

                cursor?.use {
                    val nameIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME)
                    val emailIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)

                    while (it.moveToNext()) {
                        val name = it.getString(nameIndex) ?: "Unknown"
                        val email = it.getString(emailIndex) ?: continue

                        // Only add if email is valid
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            contacts.add(Pair(name, email))
                        }
                    }
                }

                Timber.d("Fetched ${contacts.size} contacts from device")
                Result.success(contacts.distinctBy { it.second }) // Remove duplicates by email
            } catch (e: SecurityException) {
                Timber.e(e, "Permission denied to read contacts")
                Result.failure(Exception("Permission denied. Please grant contacts permission."))
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch contacts")
                Result.failure(e)
            }
        }

    /**
     * Check if READ_CONTACTS permission is granted
     */
    fun hasContactsPermission(): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}