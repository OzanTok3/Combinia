package com.ozantok.combinia.ui


import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.auth.GoogleAuthUiClient
import com.ozantok.combinia.navigation.AppNavHost
import com.ozantok.combinia.navigation.Screen
import com.ozantok.combinia.presentation.AuthViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.uiState.collectAsState().value
    val isLoggedIn =  FirebaseAuth.getInstance().currentUser != null
    val shouldShowBars = isLoggedIn && currentRoute !in listOf(Screen.Onboarding.route, Screen.Login.route)

    Scaffold(
        bottomBar = {
            if (shouldShowBars) {
                NavigationBar {
                    val items = listOf(
                        Screen.Home,
                        Screen.Search,
                        Screen.Favorites,
                        Screen.Profile
                    )
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = when (screen) {
                                        Screen.Home -> Icons.Default.Home
                                        Screen.Search -> Icons.Default.Search
                                        Screen.Favorites -> Icons.Default.Favorite
                                        Screen.Profile -> Icons.Default.Person
                                        else -> Icons.Default.Home
                                    },
                                    contentDescription = screen.route
                                )
                            },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (shouldShowBars) {
                FloatingActionButton(onClick = {
                    navController.navigate(Screen.Share.route)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Paylaş")
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            googleAuthUiClient = googleAuthUiClient,
            launcher = launcher
        )
    }
}