package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class MyVoucherResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("userid")
    var userid: String? = null,
    @SerializedName("voucherslist")
    var vouchersList: List<VouchersList>? = null
)