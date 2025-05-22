package com.ozantok.combinia.navigation

import androidx.annotation.DrawableRes
import com.ozantok.combinia.R

sealed class Screen(val route: String, val iconRes: Int) {
    object Home : Screen("home", R.drawable.icon_home)
    object Search : Screen("search", R.drawable.icon_search)
    object Share : Screen("share", R.drawable.icon_write)
    object Favorites : Screen("favorites", R.drawable.icon_favorite)
    object Profile : Screen("profile", R.drawable.icon_user)
    object Login : Screen("login", 0)
    object Onboarding : Screen("onboarding", 0)
}