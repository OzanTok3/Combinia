package com.ozantok.combinia.navigation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.auth.GoogleAuthUiClient
import com.ozantok.combinia.presentation.AuthViewModel
import com.ozantok.combinia.presentation.LoginScreen
import com.ozantok.combinia.ui.favorites.FavoritesScreen
import com.ozantok.combinia.ui.home.HomeScreen
import com.ozantok.combinia.ui.profile.ProfileScreen
import com.ozantok.combinia.ui.search.SearchScreen
import com.ozantok.combinia.ui.share.ShareScreen
import com.ozantok.combinia.ui.splash.OnboardingScreen
import com.ozantok.combinia.util.PreferencesManager

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.uiState.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

            if (isLoggedIn) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            } else {
                LoginScreen(
                    googleAuthUiClient = googleAuthUiClient,
                    launcher = launcher,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable(Screen.Home.route) {
            if (FirebaseAuth.getInstance().currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                HomeScreen()
            }
        }

        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Share.route) { ShareScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
    }
}