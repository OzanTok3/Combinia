package com.ozantok.combinia
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.util.PreferencesManager

@Suppress("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_splash)

        val prefs = PreferencesManager(this)
        val isOnboardingCompleted = prefs.isOnboardingShown()
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

        val nextRoute = when {
            !isOnboardingCompleted -> "onboarding"
            isUserLoggedIn -> "home"
            else -> "login"
        }

        window.decorView.postDelayed({
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("navigateTo", nextRoute)
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2000)
    }
}
