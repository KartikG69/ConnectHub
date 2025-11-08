package com.connecthub.presentation.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connecthub.domain.model.Contact
import com.connecthub.domain.model.User
import com.connecthub.presentation.components.Avatar
import com.connecthub.presentation.theme.ConnectHubTheme

@Composable
fun ContactItem(
    contact: Contact,
    onClick: (Contact) -> Unit,
    onToggleFavorite: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = contact.nickname ?: contact.contactUser?.displayName ?: "Unknown"
    val email = contact.contactUser?.email ?: ""
    val isOnline = contact.contactUser?.isOnline ?: false

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(contact) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with online indicator
        Box {
            Avatar(
                displayName = displayName,
                imageUrl = contact.contactUser?.photoUrl,
                size = 48.dp,
                showOnlineIndicator = false
            )

            // Online indicator
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Contact info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (contact.isFavorite) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                }
            }

            if (email.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Favorite toggle button
        IconButton(
            onClick = { onToggleFavorite(contact) }
        ) {
            Icon(
                imageVector = if (contact.isFavorite) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.StarBorder
                },
                contentDescription = if (contact.isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (contact.isFavorite) {
                    Color(0xFFFFC107)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Preview
@Composable
fun ContactItemPreview() {
    ConnectHubTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ContactItem(
                contact = Contact(
                    id = "contact-1",
                    userId = "user-1",
                    contactUserId = "user-2",
                    contactUser = User(
                        id = "user-2",
                        displayName = "Alice Johnson",
                        email = "alice@example.com",
                        isOnline = true
                    ),
                    isFavorite = true,
                    addedAt = System.currentTimeMillis()
                ),
                onClick = {},
                onToggleFavorite = {}
            )

            Divider(modifier = Modifier.padding(start = 76.dp))

            ContactItem(
                contact = Contact(
                    id = "contact-2",
                    userId = "user-1",
                    contactUserId = "user-3",
                    contactUser = User(
                        id = "user-3",
                        displayName = "Bob Smith",
                        email = "bob@example.com",
                        isOnline = false
                    ),
                    isFavorite = false,
                    addedAt = System.currentTimeMillis()
                ),
                onClick = {},
                onToggleFavorite = {}
            )
        }
    }
}