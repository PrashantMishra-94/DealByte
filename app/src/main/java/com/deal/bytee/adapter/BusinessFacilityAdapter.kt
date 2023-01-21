package com.deal.bytee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deal.bytee.R
import com.deal.bytee.data.model.Facility
import com.deal.bytee.databinding.LytFacilitiesBinding

class BusinessFacilityAdapter(private val facilities: List<Facility?>): RecyclerView.Adapter<BusinessFacilityAdapter.ViewHolder>() {

    class ViewHolder(val binding :LytFacilitiesBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LytFacilitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            facilities[position]?.let {
                tvName.text = it.facTitle
                Glide.with(ivImage.context).load(it.image)
                    .placeholder(R.drawable.logoo)
                    .into(ivImage)
            }
        }
    }

    override fun getItemCount(): Int = facilities.size
}