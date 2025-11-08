package com.connecthub.presentation.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.connecthub.presentation.auth.AuthViewModel

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    contactsViewModel: ContactsViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val uiState by contactsViewModel.uiState.collectAsState()
    val searchQuery by contactsViewModel.searchQuery.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Initialize the contacts view model with current user ID
    LaunchedEffect(authState.currentUser?.uid) {
        authState.currentUser?.uid?.let { userId ->
            contactsViewModel.initialize(userId)
        }
    }

    // Show snackbar when there's a message
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            contactsViewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    // Sync button with tooltip
                    IconButton(
                        onClick = { contactsViewModel.onSyncGoogleContacts() },
                        enabled = !uiState.isSyncing
                    ) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sync contacts from Google account",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { contactsViewModel.onAddContactFromSearch(null) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add new contact",
                    tint = Color.White
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Search Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = contactsViewModel::onSearchQueryChange,
                        placeholder = { Text("Search contacts...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search, contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { contactsViewModel.onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        singleLine = true
                    )
                }

                // Content
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Something went wrong",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = uiState.error ?: "Unknown error",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { contactsViewModel.refreshContacts() }) {
                                Text("Retry")
                            }
                        }
                    }

                    uiState.filteredContacts.isEmpty() -> {
                        // Empty state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = if (searchQuery.isNotEmpty()) "No contacts found" else "No contacts yet",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (searchQuery.isNotEmpty()) {
                                    "Can't find \"$searchQuery\"?\nAdd them as a new contact"
                                } else {
                                    "Add contacts to start connecting with others.\nYour contacts will appear here."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            if (searchQuery.isNotEmpty()) {
                                // Show "Add this contact" button when search has no results
                                Button(
                                    onClick = { contactsViewModel.onAddContactFromSearch(searchQuery) },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add \"$searchQuery\"")
                                }
                            } else {
                                Spacer(modifier = Modifier.height(32.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    OutlinedButton(
                                        onClick = { contactsViewModel.onSyncGoogleContacts() },
                                        modifier = Modifier.weight(1f),
                                        enabled = !uiState.isSyncing
                                    ) {
                                        if (uiState.isSyncing) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp), strokeWidth = 2.dp
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Sync,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Sync")
                                    }

                                    Button(
                                        onClick = { contactsViewModel.onAddContact() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Add")
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // Contacts list with alphabetical sections
                        val contactsBySection = contactsViewModel.getContactsBySection()

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Favorites section
                            if (uiState.favoriteContacts.isNotEmpty() && searchQuery.isEmpty()) {
                                item {
                                    SectionHeader(title = "Favorites")
                                }

                                items(uiState.favoriteContacts) { contact ->
                                    ContactItem(
                                        contact = contact,
                                        onClick = contactsViewModel::onContactClick,
                                        onToggleFavorite = contactsViewModel::onToggleFavorite
                                    )

                                    if (contact != uiState.favoriteContacts.last()) {
                                        Divider(
                                            modifier = Modifier.padding(start = 76.dp),
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                    }
                                }

                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            // All contacts section (alphabetical)
                            if (searchQuery.isEmpty()) {
                                item {
                                    SectionHeader(title = "All Contacts")
                                }
                            }

                            contactsBySection.forEach { (section, contacts) ->
                                // Section header (letter)
                                item(key = "section-$section") {
                                    Text(
                                        text = section,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }

                                // Contacts in this section
                                items(contacts, key = { it.id }) { contact ->
                                    ContactItem(
                                        contact = contact,
                                        onClick = contactsViewModel::onContactClick,
                                        onToggleFavorite = contactsViewModel::onToggleFavorite
                                    )

                                    if (contact != contacts.last()) {
                                        Divider(
                                            modifier = Modifier.padding(start = 76.dp),
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }

                            // Add some bottom padding for the FAB
                            item {
                                Spacer(modifier = Modifier.height(88.dp))
                            }
                        }
                    }
                }
            }
        }

        // Add Contact Dialog
        if (uiState.showAddContactDialog) {
            AddContactDialog(
                onDismiss = contactsViewModel::onDismissAddContactDialog,
                onAddContact = { name, email ->
                    contactsViewModel.addContact(name, email)
                },
                prefilledName = uiState.prefilledContactName
            )
        }
    }
}