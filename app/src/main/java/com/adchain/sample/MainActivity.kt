package com.adchain.sample

import android.content.Context
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
    private lateinit var initSdkButton: MaterialButton
    private lateinit var userIdInput: TextInputEditText
    private lateinit var userIdInputLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton
    private lateinit var skipLoginButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var userInfoText: TextView
    private var isSkipMode = false  // Track if user skipped login for testing
    private var isSdkInitialized = false  // Track SDK initialization status
    
    private lateinit var quizButton: MaterialButton
    private lateinit var missionButton: MaterialButton
    private lateinit var adchainHubButton: MaterialButton
    private lateinit var bannerButton: MaterialButton
    private lateinit var adjoeButton: MaterialButton

    // App Launch Test
    private lateinit var appLaunchInput: TextInputEditText
    private lateinit var appLaunchInputLayout: TextInputLayout
    private lateinit var addTestButton: MaterialButton
    
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
        initSdkButton = findViewById(R.id.initSdkButton)
        userIdInput = findViewById(R.id.userIdInput)
        userIdInputLayout = findViewById(R.id.userIdInputLayout)
        loginButton = findViewById(R.id.loginButton)
        skipLoginButton = findViewById(R.id.skipLoginButton)
        logoutButton = findViewById(R.id.logoutButton)
        userInfoText = findViewById(R.id.userInfoText)

        quizButton = findViewById(R.id.quizButton)
        missionButton = findViewById(R.id.missionButton)
        adchainHubButton = findViewById(R.id.adchainHubButton)
        bannerButton = findViewById(R.id.bannerButton)
        adjoeButton = findViewById(R.id.adjoeButton)

        // App Launch Test
        appLaunchInput = findViewById(R.id.appLaunchInput)
        appLaunchInputLayout = findViewById(R.id.appLaunchInputLayout)
        addTestButton = findViewById(R.id.addTestButton)
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
            AdchainSdk.openOfferwall(
                context = this,
                placementId = "main_adchain_hub",
                callback = object : com.adchain.sdk.offerwall.OfferwallCallback {
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
                }
            )
        }
        
        bannerButton.setOnClickListener {
            performBannerTest()
        }

        adjoeButton.setOnClickListener {
            performAdjoeTest()
        }

        addTestButton.setOnClickListener {
            performAddTestButton()
        }
    }

    private fun performAdjoeTest() {
        Log.d(TAG, "Starting Adjoe Offerwall Test")

        // SDK의 Adjoe API 호출
        AdchainSdk.openAdjoeOfferwall(
            context = this,
            placementId = "main_adjoe_test",
            callback = object : com.adchain.sdk.offerwall.OfferwallCallback {
                override fun onOpened() {
                    Log.d(TAG, "Adjoe Offerwall opened successfully")
                }

                override fun onClosed() {
                    Log.d(TAG, "Adjoe Offerwall closed by user")
                }

                override fun onError(message: String) {
                    Log.e(TAG, "Adjoe Offerwall error: $message")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Adjoe Error: $message", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onRewardEarned(amount: Int) {
                    Log.d(TAG, "Adjoe reward earned: $amount")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Adjoe reward: $amount points!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
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
            updateUI()
        } catch (e: Exception) {
            Log.e(TAG, "SDK initialization failed", e)
            Toast.makeText(this, "SDK initialization failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performSkipLogin() {
        Log.d(TAG, "Skipping login for testing without SDK initialization")
        isSkipMode = true
        Toast.makeText(this, "Test mode: SDK not initialized, testing graceful error handling", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun performLogout() {
        Log.d(TAG, "Performing logout")

        // Actual SDK logout
        AdchainSdk.logout()
        isSkipMode = false
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
                    val linkUrl = bannerResponse.internalLinkUrl ?: bannerResponse.externalLinkUrl ?: "N/A"
                    val message = """
                        Banner Data Received:

                        Title: ${bannerResponse.titleText}
                        Image URL: ${bannerResponse.imageUrl}
                        Link Type: ${bannerResponse.linkType}
                        Link URL: $linkUrl
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

        // Update SDK init button state
        initSdkButton.isEnabled = !isSdkInitialized
        initSdkButton.text = if (isSdkInitialized) "SDK Initialized ✓" else "Initialize SDK"

        when {
            // Skip mode: Show menu without SDK initialization
            isSkipMode -> {
                loginContainer.visibility = View.GONE
                menuContainer.visibility = View.VISIBLE
                userInfoText.text = "⚠️ Test Mode: SDK Not Initialized\nTesting graceful error handling"
            }
            // Normal flow: Logged in
            isLoggedIn -> {
                loginContainer.visibility = View.GONE
                menuContainer.visibility = View.VISIBLE
                userInfoText.text = "✓ Logged in as: ${AdchainSdk.getCurrentUser()?.userId ?: "Unknown"}"
            }
            // Show login screen
            else -> {
                loginContainer.visibility = View.VISIBLE
                menuContainer.visibility = View.GONE
                userIdInput.setText("")
            }
        }
    }
    
    private fun performAddTestButton() {
        val packageName = appLaunchInput.text?.toString()?.trim()

        if (packageName.isNullOrEmpty()) {
            appLaunchInputLayout.error = "패키지명을 입력하세요 (예: com.instagram.android)"
            return
        }

        appLaunchInputLayout.error = null
        Log.d(TAG, "Preparing app launch test for package: $packageName")

        // 테스트 코드를 클립보드에 복사
        val testCode = """
window.AdchainBridge.checkAppInstalled('$packageName');
window.onAppInstalledResult = function(result) { alert('설치: ' + result.installed + '\n패키지: ' + result.identifier); };
        """.trimIndent()

        try {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Test Code", testCode)
            clipboard.setPrimaryClip(clip)

            // 안내 다이얼로그 표시
            AlertDialog.Builder(this)
                .setTitle("앱 실행 테스트 방법")
                .setMessage("""
                    테스트 코드가 클립보드에 복사되었습니다!

                    테스트 방법:
                    1. "Adchain Hub Test" 버튼을 눌러 Offerwall를 엽니다
                    2. Chrome DevTools 또는 WebView 디버깅으로 콘솔을 엽니다
                    3. 복사된 코드를 콘솔에 붙여넣고 실행합니다

                    테스트 패키지: $packageName

                    또는 아래 버튼을 눌러 Offerwall를 바로 열 수 있습니다.
                """.trimIndent())
                .setPositiveButton("Offerwall 열기") { _, _ ->
                    // Offerwall 열기
                    AdchainSdk.openOfferwall(
                        context = this,
                        placementId = "app_launch_test",
                        callback = object : com.adchain.sdk.offerwall.OfferwallCallback {
                            override fun onOpened() {
                                Log.d(TAG, "Offerwall opened for app launch test")
                                Toast.makeText(this@MainActivity, "콘솔에서 테스트 코드를 실행하세요", Toast.LENGTH_LONG).show()
                            }

                            override fun onClosed() {
                                Log.d(TAG, "Offerwall closed")
                            }

                            override fun onError(message: String) {
                                Log.e(TAG, "Offerwall error: $message")
                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onRewardEarned(amount: Int) {
                                // No-op
                            }
                        }
                    )
                }
                .setNegativeButton("취소", null)
                .show()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy test code", e)
            Toast.makeText(this, "테스트 코드 복사 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}