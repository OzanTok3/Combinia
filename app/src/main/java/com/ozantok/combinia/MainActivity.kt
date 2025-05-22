package com.ozantok.combinia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.auth.GoogleAuthUiClient
import com.ozantok.combinia.presentation.AuthViewModel
import com.ozantok.combinia.ui.MainScreen
import com.ozantok.combinia.ui.theme.CombiniaTheme
import com.ozantok.combinia.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Combinia)
        enableEdgeToEdge()

        val startDestination = intent.getStringExtra("navigateTo") ?: "home"
        val googleAuthUiClient = GoogleAuthUiClient(this)

        setContent {
            CombiniaTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val data = result.data
                    googleAuthUiClient.handleSignInResult(data) { success, user ->
                        user?.getIdToken(false)?.addOnSuccessListener { tokenResult ->
                            val idToken = tokenResult.token
                            if (success && idToken != null) {
                                authViewModel.loginWithGoogle(idToken)
                            }
                        }
                    }
                }

                MainScreen(
                    navController = navController,
                    startDestination = startDestination,
                    googleAuthUiClient = googleAuthUiClient,
                    launcher = launcher
                )
            }
        }
    }
}