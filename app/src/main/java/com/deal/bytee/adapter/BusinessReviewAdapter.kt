package com.deal.bytee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deal.bytee.R
import com.deal.bytee.data.model.Review
import com.deal.bytee.databinding.LytReviewBinding

class BusinessReviewAdapter(private val reviews: List<Review?>): RecyclerView.Adapter<BusinessReviewAdapter.ViewHolder>() {

    class ViewHolder(val binding: LytReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LytReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            reviews[position]?.let {
                Glide.with(ivImage.context).load(it.reviewerImage)
                    .placeholder(R.drawable.logoo)
                    .into(ivImage)
                tvName.text = it.reviewerName
                tvTime.text = it.time
                tvReviewtxt.text = it.reviewText
                simpleRatingBar.rating = it.rating?.toFloatOrNull() ?: 0f
            }
        }
    }

    override fun getItemCount(): Int = reviews.size
}