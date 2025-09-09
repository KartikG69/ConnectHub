package com.connecthub.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.White

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(White.copy(alpha = 0.15f))
            .border(
                width = 1.dp,
                color = White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .blur(10.dp)
    ) {
        content()
    }
}

@Preview
@Composable
fun GlassmorphicCardPreview() {
    ConnectHubTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            GlassmorphicCard(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Glassmorphic Card",
                        style = MaterialTheme.typography.titleLarge,
                        color = White
                    )
                    Text(
                        text = "This card has a blur effect and transparency",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}