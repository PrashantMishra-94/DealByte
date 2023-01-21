package com.deal.bytee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.deal.bytee.Utils.*
import com.deal.bytee.adapter.VoucherAdapter
import com.deal.bytee.data.model.ApplyVoucherRequest
import com.deal.bytee.data.model.VouchersList
import com.deal.bytee.databinding.FragmentVouchersBinding
import com.deal.bytee.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_vouchers.*

class VouchersFragment : BaseFragment() {

    companion object {
        fun instance() = VouchersFragment()
    }

    private lateinit var viewModel: VouchersViewModel
    private lateinit var binding: FragmentVouchersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVouchersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.get(this, VouchersViewModel::class.java)
        binding.apply {
            rvCartVoucher.isNestedScrollingEnabled = false
            ivBack.setOnClickListener { handleBackStack() }
            swipe.setOnRefreshListener {
                swipe.isRefreshing = false
                getVouchers()
            }
        }
        getVouchers()
    }

    private fun getVouchers() {
        if (!isLogin()) return
        viewModel.vouchers?.removeObservers(viewLifecycleOwner)
        viewModel.getVoucherList(App.sharedPreferences.getKey(MySharedPreferences.id) ?: "")
        viewModel.vouchers?.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {} //MyUtils.showProgressDialog(requireContext(), false)
                Status.ERROR -> {
                    //MyUtils.dismisProgressDialog()
                    MyUtils.showTheToastMessage(it.message)
                }
                Status.SUCCESS -> {
                    if (it.data?.status == 1) {
                        it.data.vouchersList?.let {  vouchers ->
                            rvCartVoucher.adapter = VoucherAdapter(vouchers,
                                object : VoucherAdapter.ApplyVoucher {
                                    override fun applyVoucher(voucher: VouchersList) {
                                        if (MyUtils.isNetworkAvailable()) {
                                            redeemVoucher(voucher)
                                        } else {
                                            MyUtils.showTheToastMessage("Please Check Internet Connection!")
                                        }
                                    }
                                })
                        }
                    } else {
                        MyUtils.showTheToastMessage(it.data?.message)
                    }
                }
            }
        }
    }

    private fun redeemVoucher(voucher: VouchersList) {
        viewModel.applyVoucher(ApplyVoucherRequest(
            userid = App.sharedPreferences.getKey(MySharedPreferences.id),
            amounttobeapplied = voucher.amount,
            businessid = voucher.cvBusinessId,
            vouchercode = voucher.cvCode
        )).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> MyUtils.showProgressDialog(requireContext(), false)
                Status.ERROR -> {
                    MyUtils.dismisProgressDialog()
                    MyUtils.showTheToastMessage(it.message)
                }
                Status.SUCCESS -> {
                    MyUtils.dismisProgressDialog()
                    SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Success")
                        .setContentText(it.data?.message)
                        .setConfirmText("Done")
                        .setConfirmClickListener { sweetAlertDialog ->
                            sweetAlertDialog.cancel()
                            getVouchers()
                        }
                        .show()
                }
            }
        }
    }

}