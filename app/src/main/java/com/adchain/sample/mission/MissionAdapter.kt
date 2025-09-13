package com.adchain.sample.mission

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.R
import com.adchain.sdk.core.AdchainSdk
import com.adchain.sdk.mission.AdchainMission
import com.adchain.sdk.mission.Mission
import com.adchain.sdk.mission.MissionType

class MissionAdapter(
    private val context: Context,
    private val adchainMission: AdchainMission
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_MISSION = 0
        private const val VIEW_TYPE_OFFERWALL_PROMOTION = 1
    }
    
    private var items: List<Mission> = emptyList()
    
    fun setItems(missions: List<Mission>) {
        items = missions
        notifyDataSetChanged()
    }
    
    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            MissionType.OFFERWALL_PROMOTION -> VIEW_TYPE_OFFERWALL_PROMOTION
            else -> VIEW_TYPE_MISSION
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_OFFERWALL_PROMOTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_offerwall_promotion, parent, false)
                OfferwallPromotionViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_mission, parent, false)
                MissionViewHolder(view)
            }
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mission = items[position]
        when (holder) {
            is MissionViewHolder -> holder.bind(mission, adchainMission, context)
            is OfferwallPromotionViewHolder -> holder.bind(mission, context)
        }
    }
    
    override fun getItemCount(): Int = items.size
    
    class OfferwallPromotionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        
        private val titleTextView: TextView = itemView.findViewById(R.id.promotionTitle)
        private val subtitleTextView: TextView = itemView.findViewById(R.id.promotionSubtitle)
        private val container: View = itemView.findViewById(R.id.offerwallPromotionContainer)
        
        fun bind(mission: Mission, context: Context) {
            titleTextView.text = mission.title
            subtitleTextView.text = mission.description
            
            container.setOnClickListener {
                // Open offerwall when promotion is tapped
                AdchainSdk.openOfferwall(context)
            }
        }
    }
}