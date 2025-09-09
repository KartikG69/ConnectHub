package com.connecthub.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.Error
import com.connecthub.presentation.theme.White

@Composable
fun CallControlButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean = true,
    isDanger: Boolean = false,
    size: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isDanger -> Error
        !isActive -> Error.copy(alpha = 0.8f)
        else -> White.copy(alpha = 0.2f)
    }
    
    val iconColor = when {
        isDanger -> White
        !isActive -> White
        else -> White
    }
    
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = White.copy(alpha = 0.2f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(size * 0.4f)
        )
    }
}

@Preview
@Composable
fun CallControlButtonPreview() {
    ConnectHubTheme {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                CallControlButton(
                    icon = Icons.Default.MicOff,
                    onClick = {},
                    isActive = false
                )
                
                CallControlButton(
                    icon = Icons.Default.Videocam,
                    onClick = {},
                    isActive = true
                )
                
                CallControlButton(
                    icon = Icons.Default.CallEnd,
                    onClick = {},
                    isDanger = true,
                    size = 72.dp
                )
            }
        }
    }
}