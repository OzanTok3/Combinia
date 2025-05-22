package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signIn(email, password)

    suspend fun signInWithGoogle(idToken: String) =
        repository.signInWithGoogle(idToken)
}