package com.connecthub.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.White

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(PrimaryGradient)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun FloatingActionButtonPreview() {
    ConnectHubTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {},
                icon = Icons.Default.Add
            )
        }
    }
}