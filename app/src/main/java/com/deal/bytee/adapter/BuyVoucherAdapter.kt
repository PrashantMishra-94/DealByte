package com.deal.bytee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deal.bytee.data.model.Voucher
import com.deal.bytee.databinding.LytBuyVoucherBinding

class BuyVoucherAdapter(
    private val vouchers: List<Voucher>,
    private val businessName: String,
    private val removeVoucherListener: RemoveVoucherListener
) : RecyclerView.Adapter<BuyVoucherAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LytBuyVoucherBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvRemove.setOnClickListener {
                removeVoucherListener.onRemoveVoucher(vouchers[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LytBuyVoucherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val voucher = vouchers[position]
            Glide.with(imageView.context).load(voucher.vSponserImage)
                .into(imageView)
            tvBusinessName.text = businessName
            tvVoucherName.text = voucher.vTitle
            tvDiscounttxt.text = "${voucher.vDiscount}%"
        }
    }

    override fun getItemCount() = vouchers.size

    interface RemoveVoucherListener {
        fun onRemoveVoucher(voucher: Voucher)
    }

}