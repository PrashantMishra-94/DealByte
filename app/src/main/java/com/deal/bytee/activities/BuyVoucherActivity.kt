package com.deal.bytee.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.deal.bytee.R
import com.deal.bytee.Utils.App
import com.deal.bytee.Utils.MySharedPreferences
import com.deal.bytee.Utils.MyUtils
import com.deal.bytee.Utils.Status
import com.deal.bytee.adapter.BuyVoucherAdapter
import com.deal.bytee.data.model.Voucher
import com.deal.bytee.databinding.ActivityBuyVoucherBinding
import com.deal.bytee.viewModel.BuyVoucherViewModel
import com.deal.bytee.viewModel.ViewModelFactory

class BuyVoucherActivity : BaseActivity() {

    lateinit var binding: ActivityBuyVoucherBinding
    lateinit var voucher: Voucher
    lateinit var businessName: String
    lateinit var businessId: String
    private lateinit var viewModel: BuyVoucherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.get(this, BuyVoucherViewModel::class.java)
        voucher = intent.getParcelableExtra(VOUCHER_DETAILS)!!
        businessName = intent.getStringExtra(BUSINESS_NAME)!!
        businessId = intent.getStringExtra(BUSINESS_ID)!!
        binding.cvBack.setOnClickListener { handleBackStack() }
        binding.btnPay.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Buy")
                .setContentText("Are you sure want to Buy Voucher?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .setCancelClickListener { sDialog -> sDialog.cancel() }
                .setConfirmClickListener { sweetAlertDialog ->
                    sweetAlertDialog.cancel()
                    buyVoucher()
                }.show()
        }
        binding.rvVoucehrs.adapter = BuyVoucherAdapter(
            arrayListOf(voucher), businessName,
            object : BuyVoucherAdapter.RemoveVoucherListener {
                override fun onRemoveVoucher(voucher: Voucher) {
                    SweetAlertDialog(this@BuyVoucherActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Remove")
                        .setContentText("Are you sure want to Remove?")
                        .setCancelText("Cancel")
                        .setConfirmText("Yes")
                        .setCancelClickListener { sDialog -> sDialog.cancel() }
                        .setConfirmClickListener { sweetAlertDialog ->
                            sweetAlertDialog.cancel()
                            handleBackStack()
                        }
                        .show()
                }
            })
    }

    private fun buyVoucher() {
        viewModel.buyVoucher(
            userId = App.sharedPreferences.getKey(MySharedPreferences.id),
            voucherId = voucher.vId!!,
            businessId = businessId
        ).observe(this) {
            when (it.status) {
                Status.LOADING -> MyUtils.showProgressDialog(this, false)
                Status.ERROR -> {
                    MyUtils.dismisProgressDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    if (it.data?.status == 1) {
                        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Done!")
                            .setContentText(
                                """
                                Thank You For Buying Voucher!
                                ${it.data.message}
                                """.trimIndent()
                            )
                            .setConfirmText("OKAY")
                            .setConfirmClickListener { sweetAlertDialog ->
                                sweetAlertDialog.cancel()
                                val intent = Intent(this, DashBoardActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.putExtra(DashBoardActivity.OPEN_CART, true)
                                startActivity(intent)
                            }
                            .show()
                    }
                }
            }
        }
    }

    companion object {
        const val VOUCHER_DETAILS = "VOUCHER_DETAILS"
        const val BUSINESS_ID = "BUSINESS_ID"
        const val BUSINESS_NAME = "BUSINESS_NAME"

        fun startActivity(activity: FragmentActivity, voucher: Voucher, businessId: String, businessName: String) {
            val intent = Intent(activity, BuyVoucherActivity::class.java)
            intent.putExtra(VOUCHER_DETAILS, voucher)
            intent.putExtra(BUSINESS_ID, businessId)
            intent.putExtra(BUSINESS_NAME, businessName)
            activity.startActivity(intent)
        }
    }
}