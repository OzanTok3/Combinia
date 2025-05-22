package com.ozantok.combinia.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.R
import com.ozantok.combinia.auth.GoogleAuthUiClient
import com.ozantok.combinia.navigation.AppNavHost
import com.ozantok.combinia.navigation.Screen
import com.ozantok.combinia.presentation.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val shouldShowBars = isLoggedIn && currentRoute !in listOf(Screen.Onboarding.route, Screen.Login.route)

    val items = listOf(
        Screen.Home to "Anasayfa",
        Screen.Search to "Keşfet",
        Screen.Share to "Paylaş",
        Screen.Favorites to "Favoriler",
        Screen.Profile to "Profil"
    )

    Scaffold(
        topBar = {
            if (shouldShowBars) {
                CenterAlignedTopAppBar(
                    title = {Text("Combinia", color = colorResource(id = R.color.nearly_white))},
                    actions = {
                        TextButton(onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }) {
                            Text("Çıkış",color = colorResource(id = R.color.nearly_white))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colorResource(id = R.color.beige)
                    )
                )
            }
        },
        bottomBar = {
            if (shouldShowBars) {
                NavigationBar(
                    containerColor = colorResource(id = R.color.beige),
                    tonalElevation = 0.dp
                ) {
                    items.forEach { (screen, label) ->
                        NavigationBarItem(
                            icon = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = screen.iconRes),
                                        contentDescription = screen.route,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1
                                    )
                                }
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
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorResource(id = R.color.nearly_white),
                                unselectedIconColor = colorResource(id = R.color.dark_beige),
                                selectedTextColor = colorResource(id = R.color.nearly_white),
                                unselectedTextColor = colorResource(id = R.color.dark_beige),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
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
