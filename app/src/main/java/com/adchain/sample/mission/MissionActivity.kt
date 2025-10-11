package com.adchain.sample.mission

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.R
import com.adchain.sdk.mission.AdchainMission
import com.adchain.sdk.mission.MissionStatus
import com.adchain.sdk.mission.Mission
import com.adchain.sdk.mission.MissionProgress
import com.adchain.sdk.common.AdchainAdError

class MissionActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MissionActivity"
    }
    
    // Progress UI
    private lateinit var progressCard: CardView
    private lateinit var checkBox1: ImageView
    private lateinit var checkBox2: ImageView
    private lateinit var checkBox3: ImageView
    private lateinit var progressLine1: View
    private lateinit var progressLine2: View
    
    // Mission List UI
    private lateinit var missionRecyclerView: RecyclerView
    private lateinit var missionProgressBar: ProgressBar
    private lateinit var missionEmptyText: TextView
    private lateinit var missionEmptyBanner: View
    private lateinit var missionErrorContainer: View
    private lateinit var retryButton: View
    
    // Reward UI
    private lateinit var rewardContainer: LinearLayout
    private lateinit var claimRewardButton: MaterialButton
    
    // Mission SDK
    private lateinit var missionAdapter: MissionAdapter
    private var adchainMission: AdchainMission? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mission System"
        
        initializeViews()
        setupRecyclerView()
        loadMissionData()
    }
    
    private fun initializeViews() {
        // Progress views
        progressCard = findViewById(R.id.progressCard)
        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkBox3)
        progressLine1 = findViewById(R.id.progressLine1)
        progressLine2 = findViewById(R.id.progressLine2)
        
        // Mission list views
        missionRecyclerView = findViewById(R.id.missionRecyclerView)
        missionProgressBar = findViewById(R.id.missionProgressBar)
        missionEmptyText = findViewById(R.id.missionEmptyText)
        missionEmptyBanner = findViewById(R.id.missionEmptyBanner)
        missionErrorContainer = findViewById(R.id.missionErrorContainer)
        retryButton = findViewById(R.id.retryButton)
        
        // Reward views
        rewardContainer = findViewById(R.id.rewardContainer)
        claimRewardButton = findViewById(R.id.claimRewardButton)
        
        // Set click listeners
        missionEmptyBanner.setOnClickListener {
            Log.d(TAG, "Opening offerwall from empty banner")
            com.adchain.sdk.core.AdchainSdk.openOfferwall(
                context = this,
                placementId = "mission_empty_banner"
            )
        }
        
        retryButton.setOnClickListener {
            Log.d(TAG, "Retrying mission load")
            loadMissionData()
        }
    }
    
    private fun setupRecyclerView() {
        missionRecyclerView.layoutManager = LinearLayoutManager(this)
        // Mission adapter will be initialized when loading data
    }
    
    private fun loadMissionData() {
        Log.d(TAG, "Loading mission data...")
        showLoadingState()
        
        // Initialize AdchainMission
        adchainMission = AdchainMission()
        
        // Set up mission adapter
        missionAdapter = MissionAdapter(this, adchainMission!!)
        missionRecyclerView.adapter = missionAdapter
        
        // Load missions using getMissionList
        adchainMission?.getMissionList(
            onSuccess = { missions ->
                runOnUiThread {
                    // Get mission status separately
                    adchainMission?.getMissionStatus(
                        onSuccess = { status ->
                            runOnUiThread {
                                // Update progress UI
                                val progress = MissionProgress(status.current, status.total)
                                updateProgress(progress)
                                
                                when {
                                    status.isCompleted && status.total > 0 -> {
                                        Log.d(TAG, "All missions completed (${status.current}/${status.total}), showing reward button")
                                        showRewardState()
                                    }
                                    missions.isEmpty() -> {
                                        Log.d(TAG, "No missions available")
                                        showEmptyState()
                                    }
                                    else -> {
                                        Log.d(TAG, "Loaded ${missions.size} missions")
                                        showSuccessState(missions)
                                    }
                                }
                            }
                        },
                        onFailure = { error ->
                            runOnUiThread {
                                Log.e(TAG, "Failed to get mission status: ${error.message}")
                                // Still show missions if we have them
                                if (missions.isNotEmpty()) {
                                    showSuccessState(missions)
                                } else {
                                    showEmptyState()
                                }
                            }
                        }
                    )
                }
            },
            onFailure = { error ->
                runOnUiThread {
                    Log.e(TAG, "Failed to load missions: ${error.message}")
                    showErrorState()
                }
            }
        )
    }
    
    private fun updateProgress(progress: MissionProgress) {
        Log.d(TAG, "Updating progress: ${progress.current}/${progress.total}")
        
        // Update checkboxes based on completed count
        val count = progress.current
        
        // Update all checkboxes and lines based on progress
        when (count) {
            0 -> {
                // All empty
                checkBox1.setImageResource(R.drawable.ic_circle_check_empty)
                checkBox2.setImageResource(R.drawable.ic_circle_check_empty)
                checkBox3.setImageResource(R.drawable.ic_circle_check_empty)
                progressLine1.setBackgroundColor(getColor(R.color.gray_light))
                progressLine2.setBackgroundColor(getColor(R.color.gray_light))
            }
            1 -> {
                // First completed
                checkBox1.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox2.setImageResource(R.drawable.ic_circle_check_empty)
                checkBox3.setImageResource(R.drawable.ic_circle_check_empty)
                progressLine1.setBackgroundColor(getColor(R.color.gray_light))
                progressLine2.setBackgroundColor(getColor(R.color.gray_light))
            }
            2 -> {
                // First and second completed
                checkBox1.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox2.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox3.setImageResource(R.drawable.ic_circle_check_empty)
                progressLine1.setBackgroundColor(getColor(R.color.purple_500))
                progressLine2.setBackgroundColor(getColor(R.color.gray_light))
            }
            3 -> {
                // All completed
                checkBox1.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox2.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox3.setImageResource(R.drawable.ic_circle_check_filled)
                progressLine1.setBackgroundColor(getColor(R.color.purple_500))
                progressLine2.setBackgroundColor(getColor(R.color.purple_500))
            }
            else -> {
                // For any count > 3, show all completed
                checkBox1.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox2.setImageResource(R.drawable.ic_circle_check_filled)
                checkBox3.setImageResource(R.drawable.ic_circle_check_filled)
                progressLine1.setBackgroundColor(getColor(R.color.purple_500))
                progressLine2.setBackgroundColor(getColor(R.color.purple_500))
            }
        }
    }
    
    private fun showLoadingState() {
        missionProgressBar.visibility = View.VISIBLE
        missionRecyclerView.visibility = View.GONE
        missionEmptyText.visibility = View.GONE
        missionEmptyBanner.visibility = View.GONE
        missionErrorContainer.visibility = View.GONE
        rewardContainer.visibility = View.GONE
    }
    
    private fun showSuccessState(missions: List<Mission>) {
        missionProgressBar.visibility = View.GONE
        missionRecyclerView.visibility = View.VISIBLE
        missionEmptyText.visibility = View.GONE
        missionEmptyBanner.visibility = View.GONE
        missionErrorContainer.visibility = View.GONE
        rewardContainer.visibility = View.GONE
        missionAdapter.setItems(missions)
    }
    
    private fun showEmptyState() {
        missionProgressBar.visibility = View.GONE
        missionRecyclerView.visibility = View.GONE
        missionEmptyText.visibility = View.GONE
        missionEmptyBanner.visibility = View.VISIBLE
        missionErrorContainer.visibility = View.GONE
        rewardContainer.visibility = View.GONE
    }
    
    private fun showErrorState() {
        missionProgressBar.visibility = View.GONE
        missionRecyclerView.visibility = View.GONE
        missionEmptyText.visibility = View.GONE
        missionEmptyBanner.visibility = View.GONE
        missionErrorContainer.visibility = View.VISIBLE
        rewardContainer.visibility = View.GONE
    }
    
    private fun showRewardState() {
        missionProgressBar.visibility = View.GONE
        missionRecyclerView.visibility = View.GONE
        missionEmptyText.visibility = View.GONE
        missionEmptyBanner.visibility = View.GONE
        missionErrorContainer.visibility = View.GONE
        rewardContainer.visibility = View.VISIBLE
        
        // Set up claim reward button click
        claimRewardButton.setOnClickListener {
            Log.d(TAG, "Claim reward button clicked")
            adchainMission?.clickGetReward()
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}