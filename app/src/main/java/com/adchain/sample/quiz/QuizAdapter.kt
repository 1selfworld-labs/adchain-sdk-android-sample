package com.adchain.sample.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adchain.sample.R
import com.adchain.sdk.quiz.AdchainQuiz
import com.adchain.sdk.quiz.models.QuizEvent

class QuizAdapter(
    private val context: Context,
    private val quiz: AdchainQuiz
) : RecyclerView.Adapter<QuizViewHolder>() {
    
    private val items = mutableListOf<QuizEvent>()
    
    fun setItems(newItems: List<QuizEvent>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(items[position], quiz, context)
    }
    
    override fun getItemCount(): Int = items.size
}