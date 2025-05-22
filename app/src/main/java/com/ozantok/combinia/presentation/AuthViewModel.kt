package com.ozantok.combinia.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.domain.repository.AuthRepository
import com.ozantok.combinia.domain.usecase.SignInUseCase
import com.ozantok.combinia.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = signInUseCase(email, password)
            _uiState.value = when {
                result.isSuccess -> AuthUiState(isSuccess = true)
                else -> AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = signUpUseCase(email, password)
            _uiState.value = when {
                result.isSuccess -> AuthUiState(isSuccess = true)
                else -> AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = signInUseCase.signInWithGoogle(idToken)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isSuccess = true)
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.resetPassword(email)
            onResult(result.isSuccess, result.exceptionOrNull()?.message)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _uiState.value = AuthUiState()
    }

}