package com.deal.bytee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deal.bytee.data.model.BuyVoucherRequest
import com.deal.bytee.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class BuyVoucherViewModel(private val repository: Repository): ViewModel() {

    fun buyVoucher(userId: String, voucherId: String, businessId: String) = repository.buyVoucher(
        BuyVoucherRequest(userid = userId, voucherid = voucherId, businessid = businessId),
        viewModelScope.coroutineContext + Dispatchers.IO
    )

}