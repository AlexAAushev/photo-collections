package com.practicum.practice_work_1_1.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.practice_work_1_1.R

class OnBoardingAdapter(private val onBoardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        return OnBoardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        holder.bind(onBoardingItems[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    inner class OnBoardingItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageOnBoarding = view.findViewById<ImageView>(R.id.imageOnBoarding)
        private val textTitle = view.findViewById<TextView>(R.id.textTitle)

        fun bind(onBoardingItem: OnBoardingItem) {
            imageOnBoarding.setImageResource(onBoardingItem.onBoardingImage)
            textTitle.text = onBoardingItem.title
        }
    }
}

data class OnBoardingItem(
    val onBoardingImage: Int,
    val title: String,
    val description: String
)