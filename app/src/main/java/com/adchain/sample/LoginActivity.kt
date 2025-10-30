package com.adchain.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.core.AdchainSdkLoginListener
import com.adchain.sdk.core.AdchainSdkUser
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var initSdkButton: MaterialButton
    private lateinit var userIdInput: TextInputEditText
    private lateinit var userIdInputLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton
    private lateinit var skipLoginButton: MaterialButton
    private var isSdkInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        initSdkButton = findViewById(R.id.initSdkButton)
        userIdInput = findViewById(R.id.userIdInput)
        userIdInputLayout = findViewById(R.id.userIdInputLayout)
        loginButton = findViewById(R.id.loginButton)
        skipLoginButton = findViewById(R.id.skipLoginButton)
    }

    private fun setupListeners() {
        initSdkButton.setOnClickListener {
            performSdkInitialization()
        }

        loginButton.setOnClickListener {
            performLogin()
        }

        skipLoginButton.setOnClickListener {
            performSkipLogin()
        }
    }

    private fun performSdkInitialization() {
        if (isSdkInitialized) {
            Toast.makeText(this, "SDK already initialized", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Initializing SDK...")
        try {
            SampleApplication.instance.initializeAdchainSdk()
            isSdkInitialized = true
            Toast.makeText(this, "SDK initialized successfully", Toast.LENGTH_SHORT).show()
            initSdkButton.isEnabled = false
            initSdkButton.text = "SDK Initialized ✓"
        } catch (e: Exception) {
            Log.e(TAG, "SDK initialization failed", e)
            Toast.makeText(this, "SDK initialization failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performLogin() {
        val userId = userIdInput.text?.toString()?.trim()

        if (userId.isNullOrEmpty()) {
            userIdInputLayout.error = "Please enter a user ID"
            return
        }

        userIdInputLayout.error = null

        Log.d(TAG, "Attempting login with user ID: $userId")

        val user = AdchainSdkUser.Builder(userId)
            .setGender(AdchainSdkUser.Gender.MALE)
            .setBirthYear(1990)
            .setCustomProperty("test_user", "true")
            .build()

        AdchainSdk.login(user, object : AdchainSdkLoginListener {
            override fun onSuccess() {
                Log.d(TAG, "Login successful")
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
            }

            override fun onFailure(errorType: AdchainSdkLoginListener.ErrorType) {
                Log.e(TAG, "Login failed: $errorType")
                runOnUiThread {
                    val errorMessage = when (errorType) {
                        AdchainSdkLoginListener.ErrorType.NOT_INITIALIZED -> "SDK not initialized"
                        AdchainSdkLoginListener.ErrorType.INVALID_USER_ID -> "Invalid user ID"
                        AdchainSdkLoginListener.ErrorType.AUTHENTICATION_FAILED -> "Authentication failed"
                        AdchainSdkLoginListener.ErrorType.NETWORK_ERROR -> "Network error"
                        AdchainSdkLoginListener.ErrorType.UNKNOWN -> "Unknown error"
                    }
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun performSkipLogin() {
        Log.d(TAG, "Skipping login for testing without SDK initialization")
        Toast.makeText(this, "Test mode: SDK not initialized, testing graceful error handling", Toast.LENGTH_SHORT).show()
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
