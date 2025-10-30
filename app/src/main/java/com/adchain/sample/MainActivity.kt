package com.adchain.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adchain.sdk.core.AdchainSdk
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var bottomNav: BottomNavigationView
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in
        if (!AdchainSdk.isLoggedIn) {
            Log.d(TAG, "User not logged in, redirecting to LoginActivity")
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_main_tabs)

        bottomNav = findViewById(R.id.bottom_navigation)
        setupBottomNavigation()

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "home")
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment(), "home")
                    true
                }
                R.id.navigation_benefits -> {
                    loadFragment(BenefitsFragment(), "benefits")
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        if (currentFragmentTag == tag) {
            Log.d(TAG, "Fragment $tag already loaded, skipping")
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()

        currentFragmentTag = tag
        Log.d(TAG, "Loaded fragment: $tag")
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()

        // Check if user logged out
        if (!AdchainSdk.isLoggedIn) {
            Log.d(TAG, "User logged out, redirecting to LoginActivity")
            navigateToLogin()
        }
    }
}
