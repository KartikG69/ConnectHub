package com.connecthub.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.connecthub.presentation.auth.AuthViewModel
import com.connecthub.presentation.calls.CallsScreen
import com.connecthub.presentation.chats.ChatsScreen
import com.connecthub.presentation.components.MainBottomNavigationBar
import com.connecthub.presentation.contacts.ContactsScreen
import com.connecthub.presentation.dashboard.DashboardScreen
import com.connecthub.presentation.navigation.MainScreen
import com.connecthub.presentation.settings.SettingsScreen

@Composable
fun MainAppScreen(
    authViewModel: AuthViewModel,
    mainViewModel: MainAppViewModel = hiltViewModel()
) {
    val selectedTab by mainViewModel.selectedTab.collectAsState()
    val unreadCounts by mainViewModel.unreadCounts.collectAsState()
    
    // Simulate some notifications for demo (remove this later)
    LaunchedEffect(Unit) {
        mainViewModel.simulateNotifications()
    }
    
    Scaffold(
        bottomBar = {
            MainBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { mainViewModel.selectTab(it) },
                unreadCounts = unreadCounts
            )
        }
    ) { paddingValues ->
        when (selectedTab) {
            MainScreen.Home -> {
                DashboardScreen(
                    authViewModel = authViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            MainScreen.Calls -> {
                CallsScreen()
            }
            MainScreen.Chats -> {
                ChatsScreen()
            }
            MainScreen.Contacts -> {
                ContactsScreen()
            }
            MainScreen.Settings -> {
                SettingsScreen(
                    authViewModel = authViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}