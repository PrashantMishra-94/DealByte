package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class VouchersList(
    @SerializedName("cv_applied_amount")
    var cvAppliedAmount: String? = null,
    @SerializedName("cv_business_id")
    var cvBusinessId: String? = null,
    @SerializedName("cv_business_title")
    var cvBusinessTitle: String? = null,
    @SerializedName("cv_code")
    var cvCode: String? = null,
    @SerializedName("cv_discount_percentage")
    var cvDiscountPercentage: String? = null,
    @SerializedName("cv_expiry_date")
    var cvExpiryDate: String? = null,
    @SerializedName("cv_id")
    var cvId: String? = null,
    @SerializedName("cv_min_purchase_amount")
    var cvMinPurchaseAmount: String? = null,
    @SerializedName("cv_price")
    var cvPrice: String? = null,
    @SerializedName("cv_status")
    var cvStatus: String? = null,
    @SerializedName("cv_to_be_paid")
    var cvToBePaid: Float? = null
) {
    var amount: String? = null
}