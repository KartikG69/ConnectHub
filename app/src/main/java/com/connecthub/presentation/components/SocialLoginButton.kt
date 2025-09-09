package com.connecthub.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connecthub.presentation.theme.ConnectHubTheme
import com.connecthub.presentation.theme.FacebookBlue
import com.connecthub.presentation.theme.LightGray
import com.connecthub.presentation.theme.PrimaryGradient
import com.connecthub.presentation.theme.White

enum class SocialLoginType {
    GOOGLE, PHONE, FACEBOOK
}

@Composable
fun SocialLoginButton(
    text: String,
    type: SocialLoginType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val (backgroundColor, textColor, borderColor) = when (type) {
        SocialLoginType.GOOGLE -> Triple(White, Color.Black, LightGray)
        SocialLoginType.PHONE -> Triple(Color.Transparent, White, Color.Transparent)
        SocialLoginType.FACEBOOK -> Triple(FacebookBlue, White, Color.Transparent)
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (type == SocialLoginType.PHONE) PrimaryGradient else Brush.linearGradient(listOf(backgroundColor, backgroundColor))
            )
            .border(
                width = if (type == SocialLoginType.GOOGLE) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let { iconVector ->
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 12.dp)
            )
        }
        
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun SocialLoginButtonsPreview() {
    ConnectHubTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialLoginButton(
                text = "Continue with Google",
                type = SocialLoginType.GOOGLE,
                onClick = {}
            )
            
            SocialLoginButton(
                text = "Continue with Phone",
                type = SocialLoginType.PHONE,
                onClick = {},
                icon = Icons.Default.Phone
            )
            
            SocialLoginButton(
                text = "Continue with Facebook",
                type = SocialLoginType.FACEBOOK,
                onClick = {}
            )
        }
    }
}