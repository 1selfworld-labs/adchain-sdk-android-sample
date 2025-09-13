package com.adchain.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.adchain.sample.mission.MissionActivity
import com.adchain.sample.quiz.QuizActivity
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.core.AdchainSdkUser
import com.adchain.sdk.core.AdchainSdkLoginListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    private lateinit var loginContainer: LinearLayout
    private lateinit var menuContainer: LinearLayout
    private lateinit var userIdInput: TextInputEditText
    private lateinit var userIdInputLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var userInfoText: TextView
    
    private lateinit var quizButton: MaterialButton
    private lateinit var missionButton: MaterialButton
    private lateinit var adchainHubButton: MaterialButton
    private lateinit var bannerButton: MaterialButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupListeners()
        updateUI()
    }
    
    private fun initializeViews() {
        loginContainer = findViewById(R.id.loginContainer)
        menuContainer = findViewById(R.id.menuContainer)
        userIdInput = findViewById(R.id.userIdInput)
        userIdInputLayout = findViewById(R.id.userIdInputLayout)
        loginButton = findViewById(R.id.loginButton)
        logoutButton = findViewById(R.id.logoutButton)
        userInfoText = findViewById(R.id.userInfoText)
        
        quizButton = findViewById(R.id.quizButton)
        missionButton = findViewById(R.id.missionButton)
        adchainHubButton = findViewById(R.id.adchainHubButton)
        bannerButton = findViewById(R.id.bannerButton)
    }
    
    private fun setupListeners() {
        loginButton.setOnClickListener {
            performLogin()
        }
        
        logoutButton.setOnClickListener {
            performLogout()
        }
        
        quizButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
        
        missionButton.setOnClickListener {
            startActivity(Intent(this, MissionActivity::class.java))
        }
        
        adchainHubButton.setOnClickListener {
            // Use the new SDK API to open offerwall directly
            AdchainSdk.openOfferwall(this, object : com.adchain.sdk.offerwall.OfferwallCallback {
                override fun onOpened() {
                    Log.d(TAG, "Offerwall opened successfully")
                }
                
                override fun onClosed() {
                    Log.d(TAG, "Offerwall closed by user")
                }
                
                override fun onError(message: String) {
                    Log.e(TAG, "Offerwall error: $message")
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
                
                override fun onRewardEarned(amount: Int) {
                    Log.d(TAG, "User earned reward: $amount")
                    Toast.makeText(this@MainActivity, "You earned $amount points!", Toast.LENGTH_SHORT).show()
                }
            })
        }
        
        bannerButton.setOnClickListener {
            performBannerTest()
        }
    }
    
    private fun performLogin() {
        val userId = userIdInput.text?.toString()?.trim()
        
        if (userId.isNullOrEmpty()) {
            userIdInputLayout.error = "Please enter a user ID"
            return
        }
        
        userIdInputLayout.error = null
        
        // Actual SDK login implementation
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
                    Toast.makeText(this@MainActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    updateUI()
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
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    
    private fun performLogout() {
        Log.d(TAG, "Performing logout")
        
        // Actual SDK logout
        AdchainSdk.logout()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        updateUI()
    }
    
    private fun performBannerTest() {
        Log.d(TAG, "Starting Banner Test")
        
        // SDK의 Banner API 호출
        com.adchain.sdk.banner.AdchainBanner.getBanner(
            placementId = "test_placement_001",  // 테스트용 placement ID
            onSuccess = { bannerResponse ->
                Log.d(TAG, "Banner loaded successfully")
                runOnUiThread {
                    // 팝업으로 Banner 데이터 표시
                    val message = """
                        Banner Data Received:
                        
                        Title: ${bannerResponse.titleText}
                        Image URL: ${bannerResponse.imageUrl}
                        Link URL: ${bannerResponse.linkUrl}
                    """.trimIndent()
                    
                    AlertDialog.Builder(this)
                        .setTitle("Banner Test Result")
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Banner load failed: $error")
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Banner Test Failed")
                        .setMessage("Error: ${error}")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        )
    }
    
    private fun updateUI() {
        val isLoggedIn = AdchainSdk.isLoggedIn
        
        if (isLoggedIn) {
            loginContainer.visibility = View.GONE
            menuContainer.visibility = View.VISIBLE
            userInfoText.text = "Logged in as: ${AdchainSdk.getCurrentUser()?.userId ?: "Unknown"}"
        } else {
            loginContainer.visibility = View.VISIBLE
            menuContainer.visibility = View.GONE
            userIdInput.setText("")
        }
    }
    
    override fun onResume() {
        super.onResume()
        updateUI()
    }
}