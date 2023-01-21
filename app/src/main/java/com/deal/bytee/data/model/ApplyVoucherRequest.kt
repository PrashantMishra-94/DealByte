package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class ApplyVoucherRequest(
    @SerializedName("amounttobeapplied")
    var amounttobeapplied: String? = null,
    @SerializedName("businessid")
    var businessid: String? = null,
    @SerializedName("userid")
    var userid: String? = null,
    @SerializedName("vouchercode")
    var vouchercode: String? = null
)