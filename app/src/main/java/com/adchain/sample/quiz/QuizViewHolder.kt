package com.adchain.sample.quiz

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.R
import com.adchain.sdk.quiz.AdchainQuiz
import com.adchain.sdk.quiz.models.QuizEvent
import com.bumptech.glide.Glide

class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    private val iconImageView: ImageView = itemView.findViewById(R.id.quizIcon)
    private val titleTextView: TextView = itemView.findViewById(R.id.quizTitle)
    private val pointsTextView: TextView = itemView.findViewById(R.id.quizPoints)
    private val containerView: View = itemView.findViewById(R.id.quizContainer)
    
    fun bind(quizEvent: QuizEvent, quiz: AdchainQuiz, context: Context) {
        // 직접 View 바인딩
        titleTextView.text = quizEvent.title
        pointsTextView.text = quizEvent.point
        
        // 이미지 로드
        if (quizEvent.imageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(quizEvent.imageUrl)
                .placeholder(R.drawable.ic_coin)
                .error(R.drawable.ic_coin)
                .into(iconImageView)
        } else {
            iconImageView.setImageResource(R.drawable.ic_coin)
        }
        
        // 클릭 리스너 설정
        containerView.setOnClickListener {
            quiz.clickQuiz(quizEvent.id)
        }
    }
}