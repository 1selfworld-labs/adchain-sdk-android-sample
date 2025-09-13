package com.adchain.sample.quiz

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.quiz.QuizAdapter
import com.adchain.sdk.quiz.AdchainQuiz
import com.adchain.sdk.quiz.models.QuizEvent
import kotlinx.coroutines.launch
import com.adchain.sample.R
import com.adchain.sdk.common.AdchainAdError

class QuizActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "QuizActivity"
    }
    
    // Quiz Components
    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var quizProgressBar: ProgressBar
    private lateinit var quizEmptyText: TextView
    private lateinit var quizAdapter: QuizAdapter
    private var adchainQuiz: AdchainQuiz? = null
    
    // State Views
    private lateinit var emptyBanner: View
    private lateinit var errorContainer: View
    private lateinit var retryButton: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Quiz Test"
        
        initializeViews()
        setupQuizRecyclerView()
        
        // Load quiz
        loadQuizEvents()
    }
    
    private fun initializeViews() {
        // Quiz views
        quizRecyclerView = findViewById(R.id.quizRecyclerView)
        quizProgressBar = findViewById(R.id.quizProgressBar)
        quizEmptyText = findViewById(R.id.quizEmptyText)
        
        // State views
        emptyBanner = findViewById(R.id.emptyBanner)
        errorContainer = findViewById(R.id.errorContainer)
        retryButton = findViewById(R.id.retryButton)
        
        // Set click listeners
        emptyBanner.setOnClickListener {
            // Open offerwall main
            Log.d(TAG, "Opening offerwall from empty banner")
            com.adchain.sdk.core.AdchainSdk.openOfferwall(this)
        }
        
        retryButton.setOnClickListener {
            Log.d(TAG, "Retrying quiz load")
            loadQuizEvents()
        }
    }
    
    private fun setupQuizRecyclerView() {
        quizRecyclerView.layoutManager = LinearLayoutManager(this)
        // Quiz adapter will be initialized when loading quiz events
    }
    
    private fun loadQuizEvents() {
        Log.d(TAG, "Loading quiz events...")
        showLoadingState()
        
        // Initialize AdchainQuiz
        adchainQuiz = AdchainQuiz("quiz_unit_id")
        
        // Set up quiz adapter
        quizAdapter = QuizAdapter(this, adchainQuiz!!)
        quizRecyclerView.adapter = quizAdapter
        
        // Load quiz events using getQuizList
        adchainQuiz?.getQuizList(
            onSuccess = { events ->
                runOnUiThread {
                    when {
                        events.isEmpty() -> {
                            Log.d(TAG, "No quiz events available")
                            showEmptyState()
                        }
                        else -> {
                            Log.d(TAG, "Loaded ${events.size} quiz events")
                            showSuccessState(events)
                        }
                    }
                }
            },
            onFailure = { error ->
                runOnUiThread {
                    Log.e(TAG, "Failed to load quiz events: ${error.message}")
                    showErrorState()
                }
            }
        )
    }
    
    private fun showLoadingState() {
        quizProgressBar.visibility = View.VISIBLE
        quizRecyclerView.visibility = View.GONE
        quizEmptyText.visibility = View.GONE
        emptyBanner.visibility = View.GONE
        errorContainer.visibility = View.GONE
    }
    
    private fun showSuccessState(events: List<QuizEvent>) {
        quizProgressBar.visibility = View.GONE
        quizRecyclerView.visibility = View.VISIBLE
        quizEmptyText.visibility = View.GONE
        emptyBanner.visibility = View.GONE
        errorContainer.visibility = View.GONE
        quizAdapter.setItems(events.take(3)) // Show max 3 items
    }
    
    private fun showEmptyState() {
        quizProgressBar.visibility = View.GONE
        quizRecyclerView.visibility = View.GONE
        quizEmptyText.visibility = View.GONE
        emptyBanner.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
    }
    
    private fun showErrorState() {
        quizProgressBar.visibility = View.GONE
        quizRecyclerView.visibility = View.GONE
        quizEmptyText.visibility = View.GONE
        emptyBanner.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up quiz resources if needed
    }
}