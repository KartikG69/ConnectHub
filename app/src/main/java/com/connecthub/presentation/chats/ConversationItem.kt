package com.connecthub.presentation.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.connecthub.domain.model.Conversation
import com.connecthub.domain.model.ConversationType
import com.connecthub.presentation.components.Avatar
import com.connecthub.presentation.theme.ConnectHubTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: (Conversation) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = getConversationDisplayName(conversation)
    val lastMessagePreview = getLastMessagePreview(conversation)
    val timeText = formatTime(conversation.lastMessageTime)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(conversation) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Avatar(
            displayName = displayName,
            size = 56.dp,
            showOnlineIndicator = conversation.type == ConversationType.DIRECT
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Conversation name
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Time
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (conversation.unreadCount > 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Last message preview
                Text(
                    text = lastMessagePreview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Unread count badge
                if (conversation.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (conversation.unreadCount > 99) "99+" else conversation.unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun getConversationDisplayName(conversation: Conversation): String {
    return when (conversation.type) {
        ConversationType.GROUP -> conversation.name ?: "Group Chat"
        ConversationType.CHANNEL -> conversation.name ?: "Channel"
        ConversationType.DIRECT -> {
            // For direct messages, show the other participant's name
            // In a real app, you'd fetch the user's display name
            val otherParticipant =
                conversation.participants.firstOrNull { it != conversation.createdBy }
            otherParticipant?.let { "User ${it.take(8)}" } ?: "Direct Chat"
        }
    }
}

private fun getLastMessagePreview(conversation: Conversation): String {
    // TODO: Get actual last message from message data
    return when (conversation.type) {
        ConversationType.GROUP -> "Hey everyone! How are you doing?"
        ConversationType.CHANNEL -> "Welcome to the channel!"
        ConversationType.DIRECT -> "Sounds good, let's catch up soon!"
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "now" // Less than 1 minute
        diff < 3600_000 -> "${diff / 60_000}m" // Less than 1 hour
        diff < 86400_000 -> SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date(timestamp)) // Same day
        diff < 604800_000 -> SimpleDateFormat(
            "EEE",
            Locale.getDefault()
        ).format(Date(timestamp)) // This week
        else -> SimpleDateFormat("MM/dd", Locale.getDefault()).format(Date(timestamp)) // Older
    }
}

@Preview
@Composable
fun ConversationItemPreview() {
    ConnectHubTheme {
        Column {
            ConversationItem(
                conversation = Conversation(
                    id = "conv-1",
                    name = null,
                    type = ConversationType.DIRECT,
                    participants = listOf("user-1", "user-2"),
                    lastMessageTime = System.currentTimeMillis() - 300000, // 5 minutes ago
                    createdAt = System.currentTimeMillis() - 86400000,
                    createdBy = "user-1",
                    unreadCount = 3
                ),
                onClick = {}
            )

            Divider(modifier = Modifier.padding(start = 84.dp))

            ConversationItem(
                conversation = Conversation(
                    id = "group-1",
                    name = "Team Project",
                    type = ConversationType.GROUP,
                    participants = listOf("user-1", "user-2", "user-3"),
                    lastMessageTime = System.currentTimeMillis() - 3600000, // 1 hour ago
                    createdAt = System.currentTimeMillis() - 172800000,
                    createdBy = "user-1",
                    unreadCount = 0
                ),
                onClick = {}
            )
        }
    }
}