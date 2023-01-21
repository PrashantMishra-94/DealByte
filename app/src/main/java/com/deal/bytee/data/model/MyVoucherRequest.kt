package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class MyVoucherRequest(
    @SerializedName("userid")
    var userid: String? = null
)