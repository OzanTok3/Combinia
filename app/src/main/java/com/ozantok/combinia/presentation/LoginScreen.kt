package com.ozantok.combinia.presentation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.auth.GoogleAuthUiClient
import kotlinx.coroutines.delay

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}")
    return regex.matches(password)
}

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ActivityResultLauncher<Intent>,
    onLoginSuccess: () -> Unit
) {
    val state = authViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
            authViewModel.resetState()
        }
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isRegistering by remember { mutableStateOf(false) }
    var isResettingPassword by remember { mutableStateOf(false) }
    var isFormLocked by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var resetMessage by remember { mutableStateOf<String?>(null) }

    // 1. Google login veya klasik login sonrası başarılı olursa yönlendir
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
            authViewModel.resetState() // state sıfırla
        }
    }

// 2. Kayıt sonrası formu sıfırla
    LaunchedEffect(state.isSuccess, state.error) {
        if (isRegistering && (state.isSuccess || state.error != null)) {
            isFormLocked = true
            delay(2000)
            isRegistering = false
            isFormLocked = false
            showSuccess = state.isSuccess
            authViewModel.resetState()
        }
    }

// 3. Google login'de hesap yoksa kayıt moduna geç
    LaunchedEffect(state.error) {
        if (state.error?.contains("The supplied auth credential") == true) {
            isRegistering = true
            showSuccess = false
            authViewModel.resetState()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            when {
                isResettingPassword -> "Şifre Sıfırla"
                isRegistering -> "Hesap Oluştur"
                else -> "Combinia'ya Giriş Yap"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (it.isNotBlank()) emailError = null
            },
            label = { Text("E-posta") },
            isError = emailError != null,
            enabled = !isFormLocked,
            singleLine = true
        )
        if (emailError != null) {
            Text(emailError ?: "", color = MaterialTheme.colorScheme.error)
        }

        if (!isResettingPassword) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (it.isNotBlank()) passwordError = null
                },
                label = { Text("Şifre") },
                isError = passwordError != null,
                enabled = !isFormLocked,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            if (passwordError != null) {
                Text(passwordError ?: "", color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isResettingPassword -> {
                Button(onClick = {
                    if (email.isBlank() || !isValidEmail(email)) {
                        emailError = "Geçerli bir e-posta adresi girin"
                    } else {
                        isFormLocked = true
                        authViewModel.resetPassword(email) { success, message ->
                            resetMessage = if (success) {
                                "Şifre sıfırlama bağlantısı e-postanıza gönderildi"
                            } else {
                                message ?: "Şifre sıfırlama başarısız"
                            }
                            isFormLocked = false
                        }
                    }
                }, enabled = !isFormLocked) {
                    Text("Şifre Sıfırla")
                }
                TextButton(onClick = { isResettingPassword = false }, enabled = !isFormLocked) {
                    Text("Geri Dön")
                }
                resetMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
            isRegistering -> {
                Button(onClick = {
                    var hasError = false
                    if (email.isBlank() || !isValidEmail(email)) {
                        emailError = "Geçerli bir e-posta adresi girin"
                        hasError = true
                    }
                    if (password.isBlank() || !isValidPassword(password)) {
                        passwordError = "Şifre 1 büyük, 1 küçük harf ve özel karakter içermeli"
                        hasError = true
                    }
                    if (!hasError) {
                        isFormLocked = true
                        authViewModel.signUp(email, password)
                    }
                }, enabled = !isFormLocked) {
                    Text("Hesap Oluştur")
                }
                TextButton(onClick = {
                    isRegistering = false
                    showSuccess = false
                    authViewModel.resetState()
                }, enabled = !isFormLocked) {
                    Text("Zaten hesabım var")
                }
            }
            else -> {
                Button(onClick = {
                    var hasError = false
                    if (email.isBlank() || !isValidEmail(email)) {
                        emailError = "Geçerli bir e-posta adresi girin"
                        hasError = true
                    }
                    if (password.isBlank()) {
                        passwordError = "Şifre boş bırakılamaz"
                        hasError = true
                    }
                    if (!hasError) {
                        authViewModel.signIn(email, password)
                    }
                }) {
                    Text("Giriş Yap")
                }
                TextButton(onClick = {
                    isRegistering = true
                    showSuccess = false
                }) {
                    Text("Hesabın yok mu? Kayıt Ol")
                }
                TextButton(onClick = {
                    isResettingPassword = true
                    resetMessage = null
                }) {
                    Text("Şifremi Unuttum")
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Button(onClick = {
                    val intent = googleAuthUiClient.getSignInIntent()
                    launcher.launch(intent)
                }) {
                    Text("Google ile Giriş Yap")
                }
            }
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        if (showSuccess) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Kayıt başarıyla tamamlandı!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (state.error != null && !isResettingPassword) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(state.error ?: "Bir hata oluştu", color = MaterialTheme.colorScheme.error)
        }
    }
}
