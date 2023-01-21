package com.deal.bytee.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.deal.bytee.data.model.VouchersList
import com.deal.bytee.databinding.LytVoucherListBinding

class VoucherAdapter(private val vouchers: List<VouchersList>, private val applyVoucher: ApplyVoucher) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LytVoucherListBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                edtAmount.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        vouchers[absoluteAdapterPosition].amount = s?.toString()
                    }
                })
                btnUse.setOnClickListener {
                    val voucher = vouchers[absoluteAdapterPosition]
                    if (voucher.amount.isNullOrBlank()) {
                        edtAmount.requestFocus()
                        edtAmount.error = "Please enter Amount!"
                    } else {
                        applyVoucher.applyVoucher(voucher = voucher)
                    }
                }
                btnViewDetails.setOnClickListener {
                    val voucher = vouchers[absoluteAdapterPosition]
                    SweetAlertDialog(root.context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Transaction Details")
                        .setContentText(
                            """
                            Total Bill Amount: Rs.${voucher.cvAppliedAmount}
                            Discount: ${voucher.cvDiscountPercentage}%
                            Payable Amount: Rs.${voucher.cvToBePaid}
                            """.trimIndent()
                        )
                        .setConfirmText("OKAY")
                        .setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.cancel() }
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LytVoucherListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = vouchers[position]
        holder.binding.apply {
            tvName.text = voucher.cvBusinessTitle
            tvCode.text = voucher.cvCode
            tvDiscount.text = "${voucher.cvDiscountPercentage}%"
            tvExDate.text = voucher.cvExpiryDate
            tvStatus.text = voucher.cvStatus
            tvAppliedAmount.text = "Rs. ${voucher.cvAppliedAmount}"
            if (voucher.cvStatus == "Not used") {
                llRedeem.visibility = View.VISIBLE
                llpaid.visibility = View.GONE
                llViewDetails.visibility = View.GONE
            } else {
                llRedeem.visibility = View.GONE
                llpaid.visibility = View.VISIBLE
                llViewDetails.visibility = View.VISIBLE
            }
            edtAmount.setText(voucher.amount)
        }
    }

    override fun getItemCount() = vouchers.size

    interface ApplyVoucher {
        fun applyVoucher(voucher: VouchersList)
    }
}