package com.ozantok.combinia.navigation


sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Search : Screen("search")
    object Share : Screen("share")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Login : Screen("login")
}