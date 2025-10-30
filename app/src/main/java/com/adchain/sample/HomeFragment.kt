package com.adchain.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.adchain.sample.mission.MissionActivity
import com.adchain.sample.quiz.QuizActivity
import com.adchain.sdk.core.AdchainSdk
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.TextView

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private lateinit var userInfoText: TextView
    private lateinit var quizButton: MaterialButton
    private lateinit var missionButton: MaterialButton
    private lateinit var adchainHubButton: MaterialButton
    private lateinit var bannerButton: MaterialButton
    private lateinit var adjoeButton: MaterialButton
    private lateinit var nestAdsButton: MaterialButton
    private lateinit var appLaunchInput: TextInputEditText
    private lateinit var appLaunchInputLayout: TextInputLayout
    private lateinit var addTestButton: MaterialButton
    private lateinit var logoutButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupListeners()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun initializeViews(view: View) {
        userInfoText = view.findViewById(R.id.userInfoText)
        quizButton = view.findViewById(R.id.quizButton)
        missionButton = view.findViewById(R.id.missionButton)
        adchainHubButton = view.findViewById(R.id.adchainHubButton)
        bannerButton = view.findViewById(R.id.bannerButton)
        adjoeButton = view.findViewById(R.id.adjoeButton)
        nestAdsButton = view.findViewById(R.id.nestAdsButton)
        appLaunchInput = view.findViewById(R.id.appLaunchInput)
        appLaunchInputLayout = view.findViewById(R.id.appLaunchInputLayout)
        addTestButton = view.findViewById(R.id.addTestButton)
        logoutButton = view.findViewById(R.id.logoutButton)
    }

    private fun setupListeners() {
        quizButton.setOnClickListener {
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }

        missionButton.setOnClickListener {
            startActivity(Intent(requireContext(), MissionActivity::class.java))
        }

        adchainHubButton.setOnClickListener {
            AdchainSdk.openOfferwall(
                context = requireContext(),
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
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onRewardEarned(amount: Int) {
                        Log.d(TAG, "User earned reward: $amount")
                        Toast.makeText(requireContext(), "You earned $amount points!", Toast.LENGTH_SHORT).show()
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

        nestAdsButton.setOnClickListener {
            performNestAdsTest()
        }

        addTestButton.setOnClickListener {
            performAddTestButton()
        }

        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun updateUI() {
        val isLoggedIn = AdchainSdk.isLoggedIn
        val currentUser = AdchainSdk.getCurrentUser()

        userInfoText.text = if (isLoggedIn && currentUser != null) {
            "✓ Logged in as: ${currentUser.userId}"
        } else {
            "⚠️ Not logged in"
        }
    }

    private fun performBannerTest() {
        Log.d(TAG, "Starting Banner Test")

        com.adchain.sdk.banner.AdchainBanner.getBanner(
            placementId = "test_placement_001",
            onSuccess = { bannerResponse ->
                Log.d(TAG, "Banner loaded successfully")
                val linkUrl = bannerResponse.internalLinkUrl ?: bannerResponse.externalLinkUrl ?: "N/A"
                val message = """
                    Banner Data Received:

                    Title: ${bannerResponse.titleText}
                    Image URL: ${bannerResponse.imageUrl}
                    Link Type: ${bannerResponse.linkType}
                    Link URL: $linkUrl
                """.trimIndent()

                AlertDialog.Builder(requireContext())
                    .setTitle("Banner Test Result")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            },
            onFailure = { error ->
                Log.e(TAG, "Banner load failed: $error")
                AlertDialog.Builder(requireContext())
                    .setTitle("Banner Test Failed")
                    .setMessage("Error: $error")
                    .setPositiveButton("OK", null)
                    .show()
            }
        )
    }

    private fun performAdjoeTest() {
        Log.d(TAG, "Starting Adjoe Offerwall Test")

        AdchainSdk.openAdjoeOfferwall(
            context = requireContext(),
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
                    Toast.makeText(requireContext(), "Adjoe Error: $message", Toast.LENGTH_LONG).show()
                }

                override fun onRewardEarned(amount: Int) {
                    Log.d(TAG, "Adjoe reward earned: $amount")
                    Toast.makeText(requireContext(), "Adjoe reward: $amount points!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun performNestAdsTest() {
        Log.d(TAG, "Starting NestAds Offerwall Test")

        AdchainSdk.openOfferwallNestAds(
            context = requireContext(),
            placementId = "test_nest_ads_placement",
            callback = object : com.adchain.sdk.offerwall.OfferwallCallback {
                override fun onOpened() {
                    Log.d(TAG, "NestAds Offerwall opened successfully")
                }

                override fun onClosed() {
                    Log.d(TAG, "NestAds Offerwall closed by user")
                }

                override fun onError(message: String) {
                    Log.e(TAG, "NestAds Offerwall error: $message")
                    Toast.makeText(requireContext(), "NestAds Error: $message", Toast.LENGTH_LONG).show()
                }

                override fun onRewardEarned(amount: Int) {
                    Log.d(TAG, "NestAds reward earned: $amount")
                    Toast.makeText(requireContext(), "NestAds reward: $amount points!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun performAddTestButton() {
        val packageName = appLaunchInput.text?.toString()?.trim()

        if (packageName.isNullOrEmpty()) {
            appLaunchInputLayout.error = "패키지명을 입력하세요 (예: com.instagram.android)"
            return
        }

        appLaunchInputLayout.error = null
        Log.d(TAG, "Preparing app launch test for package: $packageName")

        val testCode = """
window.AdchainBridge.checkAppInstalled('$packageName');
window.onAppInstalledResult = function(result) { alert('설치: ' + result.installed + '\n패키지: ' + result.identifier); };
        """.trimIndent()

        try {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Test Code", testCode)
            clipboard.setPrimaryClip(clip)

            AlertDialog.Builder(requireContext())
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
                    AdchainSdk.openOfferwall(
                        context = requireContext(),
                        placementId = "app_launch_test",
                        callback = object : com.adchain.sdk.offerwall.OfferwallCallback {
                            override fun onOpened() {
                                Log.d(TAG, "Offerwall opened for app launch test")
                                Toast.makeText(requireContext(), "콘솔에서 테스트 코드를 실행하세요", Toast.LENGTH_LONG).show()
                            }

                            override fun onClosed() {
                                Log.d(TAG, "Offerwall closed")
                            }

                            override fun onError(message: String) {
                                Log.e(TAG, "Offerwall error: $message")
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "테스트 코드 복사 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogout() {
        Log.d(TAG, "Performing logout")
        AdchainSdk.logout()
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
