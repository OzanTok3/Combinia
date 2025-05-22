package com.ozantok.combinia.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.ozantok.combinia.R

class GoogleAuthUiClient(
    private val context: Context
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // firebase console'dan
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(context, gso)
        return client.signInIntent
    }

    fun handleSignInResult(data: Intent?, onComplete: (Boolean, FirebaseUser?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener {
                    onComplete(it.isSuccessful, auth.currentUser)
                }
        } catch (e: Exception) {
            onComplete(false, null)
        }
    }
}