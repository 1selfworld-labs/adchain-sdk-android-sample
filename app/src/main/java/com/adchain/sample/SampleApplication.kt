package com.adchain.sample

import android.app.Application
import android.util.Log
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.core.AdchainSdkConfig

/**
 * Sample Application class for Adchain SDK initialization
 */
class SampleApplication : Application() {
    
    companion object {
        private const val TAG = "AdchainSample"
        
        // Test App ID and Secret - Replace with your actual values
        private const val APP_ID = "100000001"
        private const val APP_SECRET = "gjFs586lLuUweJRN"
        
        lateinit var instance: SampleApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        initializeAdchainSdk()
    }
    
    private fun initializeAdchainSdk() {
        Log.d(TAG, "Initializing Adchain SDK...")
        
        // Actual SDK initialization
        val config = AdchainSdkConfig.Builder(APP_ID, APP_SECRET)
            .setEnvironment(AdchainSdkConfig.Environment.DEVELOPMENT) // Use PRODUCTION for release
            .setTimeout(30000L) // 30 seconds timeout
            .build()
        
        AdchainSdk.initialize(this, config)
        
        Log.d(TAG, "Adchain SDK initialized successfully with App ID: $APP_ID")
    }
}