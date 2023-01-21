package com.deal.bytee.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deal.bytee.Utils.Resource
import com.deal.bytee.data.model.ApplyVoucherRequest
import com.deal.bytee.data.model.MyVoucherRequest
import com.deal.bytee.data.model.MyVoucherResponse
import com.deal.bytee.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class VouchersViewModel(private val repository: Repository) : ViewModel() {

    var vouchers: LiveData<Resource<MyVoucherResponse>>? = null

    fun getVoucherList(userId: String) {
        vouchers = repository.getMyVoucherList(
                MyVoucherRequest(userId), viewModelScope.coroutineContext + Dispatchers.IO
        )
    }

    fun applyVoucher(applyVoucherRequest: ApplyVoucherRequest) = repository.applyVoucher(applyVoucherRequest,
        viewModelScope.coroutineContext + Dispatchers.IO)

}