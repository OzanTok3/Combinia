package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signUp(email, password)
}