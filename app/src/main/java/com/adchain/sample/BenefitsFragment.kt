package com.adchain.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.offerwall.AdchainOfferwallView
import com.adchain.sdk.offerwall.OfferwallCallback
import com.adchain.sdk.offerwall.OfferwallEventCallback

class BenefitsFragment : Fragment() {

    companion object {
        private const val TAG = "BenefitsFragment"
        private const val PLACEMENT_ID = "sample-test-android-placement"
    }

    private var offerwallView: AdchainOfferwallView? = null
    private var backPressCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_benefits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerwallView = view.findViewById(R.id.offerwallView)
        setupOfferwallView()
        setupBackPressHandler()
        loadOfferwall()
    }

    private fun setupOfferwallView() {
        offerwallView?.setCallback(object : OfferwallCallback {
            override fun onOpened() {
                Log.d(TAG, "Offerwall opened in benefits tab")
            }

            override fun onClosed() {
                Log.d(TAG, "Offerwall closed in benefits tab")
            }

            override fun onError(error: String) {
                Log.e(TAG, "Offerwall error: $error")
                Toast.makeText(requireContext(), "Offerwall error: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardEarned(amount: Int) {
                Log.d(TAG, "Reward earned: $amount")
                Toast.makeText(requireContext(), "Reward earned: $amount points!", Toast.LENGTH_SHORT).show()
            }
        })

        // Set event callback for custom events (if SDK version >= 1.0.41)
        offerwallView?.setEventCallback(object : OfferwallEventCallback {
            override fun onCustomEvent(eventType: String, payload: Map<String, Any?>) {
                Log.d(TAG, "[WebView → App] Custom Event: $eventType, payload: $payload")

                // Handle custom events from WebView
                when (eventType) {
                    "show_toast" -> {
                        val message = payload["message"] as? String ?: "No message"
                        Toast.makeText(requireContext(), "🎉 WebView Message: $message", Toast.LENGTH_SHORT).show()
                    }
                    "navigate" -> {
                        val screen = payload["screen"] as? String ?: "unknown"
                        Toast.makeText(requireContext(), "🧭 Navigate to: $screen", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "📨 Event: $eventType", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onDataRequest(requestId: String, requestType: String, params: Map<String, Any?>): Map<String, Any?>? {
                Log.d(TAG, "[WebView → App] Data Request: id=$requestId, type=$requestType, params=$params")

                // Return data based on request type
                return when (requestType) {
                    "user_points" -> mapOf("points" to 12345, "currency" to "KRW")
                    "user_profile" -> mapOf("userId" to "test_123", "nickname" to "TestPlayer", "level" to 42)
                    "app_version" -> mapOf("version" to "1.0.0", "buildNumber" to 100)
                    else -> null
                }
            }
        })
    }

    private fun setupBackPressHandler() {
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "Back button pressed in Benefits fragment")

                val handled = offerwallView?.handleBackPress() ?: false

                if (!handled) {
                    Log.d(TAG, "WebView did not handle back press, allowing default behavior")
                    // Allow default back behavior (will exit app)
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressCallback!!
        )
    }

    private fun loadOfferwall() {
        val config = AdchainSdk.getConfig()
        val user = AdchainSdk.getCurrentUser()

        if (config == null || user == null) {
            Log.e(TAG, "SDK not initialized or user not logged in")
            Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Loading offerwall with placementId: $PLACEMENT_ID")
        offerwallView?.loadOfferwall(PLACEMENT_ID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressCallback?.remove()
        offerwallView = null
    }
}
