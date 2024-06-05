package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.signupButton2.setOnClickListener {
            if (validateInput()) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()
                signUpWithEmailPassword(email, password)
            }
        }

    }

    private fun signUpWithEmailPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-up user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(baseContext, "Sign up failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateInput(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.passwordEditText2.text.toString().trim()
        val mobileNumber = binding.Mnumber.text.toString().trim()

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

        if (confirmPassword.isEmpty()) {
            binding.passwordEditText2.error = "Confirm Password is required"
            binding.passwordEditText2.requestFocus()
            return false
        }

        if (confirmPassword != password) {
            binding.passwordEditText2.error = "Passwords do not match"
            binding.passwordEditText2.requestFocus()
            return false
        }

        if (mobileNumber.isEmpty()) {
            binding.Mnumber.error = "Mobile Number is required"
            binding.Mnumber.requestFocus()
            return false
        }

        if (!Patterns.PHONE.matcher(mobileNumber).matches()) {
            binding.Mnumber.error = "Please enter a valid mobile number"
            binding.Mnumber.requestFocus()
            return false
        }

        return true
    }
}
