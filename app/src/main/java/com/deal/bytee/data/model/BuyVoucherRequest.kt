package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class BuyVoucherRequest(
    @SerializedName("businessid")
    var businessid: String? = null,
    @SerializedName("userid")
    var userid: String? = null,
    @SerializedName("voucherid")
    var voucherid: String? = null
)