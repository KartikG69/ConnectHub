package com.connecthub.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.connecthub.presentation.navigation.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomNavigationBar(
    selectedTab: MainScreen,
    onTabSelected: (MainScreen) -> Unit,
    modifier: Modifier = Modifier,
    unreadCounts: Map<MainScreen, Int> = emptyMap()
) {
    val screens = MainScreen.getAllScreens()
    
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        screens.forEach { screen ->
            val unreadCount = unreadCounts[screen] ?: 0
            
            NavigationBarItem(
                icon = {
                    if (unreadCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                ) {
                                    Text(
                                        text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selectedTab == screen) screen.selectedIcon else screen.icon,
                                contentDescription = screen.title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (selectedTab == screen) screen.selectedIcon else screen.icon,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { 
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                selected = selectedTab == screen,
                onClick = { onTabSelected(screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}