package com.ozantok.combinia.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    fun signOut()
    fun getCurrentUserId(): String?
}