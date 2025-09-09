package com.connecthub.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon
) {
    object Home : MainScreen(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    
    object Calls : MainScreen(
        route = "calls",
        title = "Calls",
        icon = Icons.Outlined.Call,
        selectedIcon = Icons.Filled.Call
    )
    
    object Chats : MainScreen(
        route = "chats",
        title = "Chats",
        icon = Icons.Outlined.ChatBubbleOutline,
        selectedIcon = Icons.Filled.ChatBubble
    )
    
    object Contacts : MainScreen(
        route = "contacts",
        title = "Contacts",
        icon = Icons.Outlined.People,
        selectedIcon = Icons.Filled.People
    )
    
    object Settings : MainScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )
    
    companion object {
        fun getAllScreens() = listOf(Home, Calls, Chats, Contacts, Settings)
        
        fun fromRoute(route: String): MainScreen = when (route) {
            Home.route -> Home
            Calls.route -> Calls
            Chats.route -> Chats
            Contacts.route -> Contacts
            Settings.route -> Settings
            else -> Home
        }
    }
}