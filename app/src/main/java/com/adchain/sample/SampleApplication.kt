package com.adchain.sample

import android.app.Application
import android.util.Log
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.core.AdchainSdkConfig
import com.adchain.sdk.utils.LogLevel

/**
 * Sample Application class for Adchain SDK initialization
 */
class SampleApplication : Application() {
    
    companion object {
        private const val TAG = "AdchainSample"
        
        // Test App ID and Secret - Replace with your actual values
        private const val APP_ID = "123456781"
        private const val APP_SECRET = "abcdefghigjk"
        
        lateinit var instance: SampleApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this

        // SDK 초기화를 자동으로 하지 않음
        // MainActivity에서 선택적으로 초기화할 수 있도록 함
        Log.d(TAG, "Application created - SDK initialization skipped for testing")
    }

    fun initializeAdchainSdk() {
        Log.d(TAG, "Initializing Adchain SDK...")

        // Set SDK log level to VERBOSE for debugging
        AdchainSdk.setLogLevel(LogLevel.VERBOSE)

        // Actual SDK initialization
        val config = AdchainSdkConfig.Builder(APP_ID, APP_SECRET)
            .setEnvironment(AdchainSdkConfig.Environment.DEVELOPMENT) // Use PRODUCTION for release
            .setTimeout(30000L) // 30 seconds timeout
            .build()

        AdchainSdk.initialize(this, config)

        Log.d(TAG, "Adchain SDK initialized successfully with App ID: $APP_ID")
    }
}