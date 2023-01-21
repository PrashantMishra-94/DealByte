package com.deal.bytee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deal.bytee.data.model.SubCategory
import com.deal.bytee.databinding.LytMenuBinding

class BusinessMenuAdapter(private val menus: List<SubCategory?>) : RecyclerView.Adapter<BusinessMenuAdapter.ViewHolder>() {

    class ViewHolder(val binding: LytMenuBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LytMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            menus[position]?.let {
                tvName.text = it.subCategoryName
                tvCount.text = "Rs. ${it.subCategoryPrice}"
            }
        }
    }

    override fun getItemCount(): Int = menus.size
}