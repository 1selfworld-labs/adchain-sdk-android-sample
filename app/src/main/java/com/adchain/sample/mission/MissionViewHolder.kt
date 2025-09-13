package com.adchain.sample.mission

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.R
import com.adchain.sdk.mission.AdchainMission
import com.adchain.sdk.mission.Mission
import com.adchain.sdk.mission.MissionType
import com.bumptech.glide.Glide

class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    private val iconImageView: ImageView = itemView.findViewById(R.id.missionIcon)
    private val titleTextView: TextView = itemView.findViewById(R.id.missionTitle)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.missionSubtitle)
    private val pointsTextView: TextView = itemView.findViewById(R.id.missionPoints)
    private val containerView: View = itemView.findViewById(R.id.missionContainer)
    
    fun bind(mission: Mission, adchainMission: AdchainMission, context: Context) {
        // 텍스트 바인딩
        titleTextView.text = mission.title
        descriptionTextView.text = mission.description
        pointsTextView.text = mission.point
        
        // 이미지 로드
        if (mission.imageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(mission.imageUrl)
                .placeholder(R.drawable.ic_coin)
                .error(R.drawable.ic_coin)
                .into(iconImageView)
        } else {
            // 타입에 따른 기본 아이콘 설정
            val defaultIcon = when (mission.type) {
                MissionType.OFFERWALL_PROMOTION -> R.drawable.ic_100_point
                else -> R.drawable.ic_coin
            }
            iconImageView.setImageResource(defaultIcon)
        }
        
        // 클릭 처리
        containerView.setOnClickListener {
            when (mission.type) {
                MissionType.OFFERWALL_PROMOTION -> {
                    // 오퍼월 프로모션인 경우 오퍼월 열기
                    com.adchain.sdk.core.AdchainSdk.openOfferwall(context)
                }
                else -> {
                    // 일반 미션인 경우 미션 클릭 처리
                    adchainMission.clickMission(mission.id)
                }
            }
        }
    }
}