package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class ApplyVoucherResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("userid")
    var userid: String? = null,
    @SerializedName("voucher_code")
    var voucherCode: String? = null,
    @SerializedName("voucher_status")
    var voucherStatus: Int? = null
)