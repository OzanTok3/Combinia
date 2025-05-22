package com.ozantok.combinia.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}
