package com.connecthub.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.Success
import com.connecthub.presentation.theme.White

@Composable
fun Avatar(
    name: String? = null,
    displayName: String? = null,
    imageUrl: String? = null,
    size: Dp = 48.dp,
    showOnlineIndicator: Boolean = false,
    modifier: Modifier = Modifier
) {
    val actualName = displayName ?: name ?: "User"
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(size / 3.5f))
                .then(
                    if (imageUrl == null) {
                        Modifier.background(PrimaryGradient)
                    } else {
                        Modifier.background(Color.Transparent)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "$actualName profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size)
                        .clip(RoundedCornerShape(size / 3.5f))
                )
            } else {
                Text(
                    text = getInitials(actualName),
                    color = White,
                    fontSize = (size.value * 0.4f).sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        if (showOnlineIndicator) {
            Box(
                modifier = Modifier
                    .size(size * 0.25f)
                    .clip(RoundedCornerShape(50))
                    .background(Success)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

private fun getInitials(name: String): String {
    return name.trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }
}

@Preview
@Composable
fun AvatarPreview() {
    ConnectHubTheme {
        Avatar(
            name = "Gandu",
            showOnlineIndicator = true
        )
    }
}

@Preview
@Composable
fun AvatarLargePreview() {
    ConnectHubTheme {
        Avatar(
            name = "Chutiye",
            size = 64.dp,
            showOnlineIndicator = false
        )
    }
}