package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, Home_Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener {
            if (validateInput()) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                loginWithEmailPassword(email, password)
            }
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Save login state
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    val intent = Intent(this, Home_Activity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateInput(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            binding.emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please enter a valid email"
            binding.emailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            binding.passwordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password must be at least 6 characters"
            binding.passwordEditText.requestFocus()
            return false
        }

        return true
    }
}
