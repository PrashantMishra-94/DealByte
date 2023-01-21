package com.deal.bytee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deal.bytee.R
import com.deal.bytee.databinding.LytBusinessGalleryBinding

class BusinessGalleryAdapter(private val pics: List<String?>): RecyclerView.Adapter<BusinessGalleryAdapter.ViewHolder>() {

    class ViewHolder(val binding: LytBusinessGalleryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LytBusinessGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        pics[position]?.let {
            Glide.with(holder.binding.ivImage.context).load(it)
                .placeholder(R.drawable.logoo)
                .into(holder.binding.ivImage)
        }
    }

    override fun getItemCount(): Int = pics.size
}