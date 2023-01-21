package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class BusinessDetails(
    @SerializedName("DetailedData")
    var detailedData: List<DetailedData?>? = null,
    @SerializedName("facilities")
    var facilities: List<Facility?>? = null,
    @SerializedName("menu")
    var menu: List<Menu?>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("morepics")
    var morepics: List<String?>? = null,
    @SerializedName("reviews")
    var reviews: List<Review?>? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("totalcount")
    var totalcount: Int? = null,
    @SerializedName("userid")
    var userid: String? = null,
    @SerializedName("voucher_bought_status")
    var voucherBoughtStatus: Int? = null,
    @SerializedName("voucher_buy_status_message")
    var voucherBuyStatusMessage: String? = null,
    @SerializedName("vouchers")
    var vouchers: List<Voucher>? = null
)