package com.connecthub.presentation.main

import androidx.lifecycle.ViewModel
import com.connecthub.presentation.navigation.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor() : ViewModel() {
    
    private val _selectedTab = MutableStateFlow<MainScreen>(MainScreen.Home)
    val selectedTab: StateFlow<MainScreen> = _selectedTab.asStateFlow()
    
    private val _unreadCounts = MutableStateFlow<Map<MainScreen, Int>>(emptyMap())
    val unreadCounts: StateFlow<Map<MainScreen, Int>> = _unreadCounts.asStateFlow()
    
    fun selectTab(tab: MainScreen) {
        _selectedTab.value = tab
        
        // Clear unread count for the selected tab
        if (_unreadCounts.value.containsKey(tab)) {
            _unreadCounts.value = _unreadCounts.value.toMutableMap().apply {
                remove(tab)
            }
        }
    }
    
    fun updateUnreadCount(screen: MainScreen, count: Int) {
        _unreadCounts.value = _unreadCounts.value.toMutableMap().apply {
            if (count > 0) {
                put(screen, count)
            } else {
                remove(screen)
            }
        }
    }
    
    fun addUnreadCount(screen: MainScreen) {
        val currentCount = _unreadCounts.value[screen] ?: 0
        updateUnreadCount(screen, currentCount + 1)
    }
    
    fun clearUnreadCount(screen: MainScreen) {
        updateUnreadCount(screen, 0)
    }
    
    // Simulate some unread notifications (for demo purposes)
    fun simulateNotifications() {
        updateUnreadCount(MainScreen.Calls, 3)
        updateUnreadCount(MainScreen.Chats, 7)
    }
}