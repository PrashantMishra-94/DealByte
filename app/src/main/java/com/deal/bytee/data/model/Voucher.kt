package com.deal.bytee.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Voucher(
    @SerializedName("v_discount")
    var vDiscount: String? = null,
    @SerializedName("v_id")
    var vId: String? = null,
    @SerializedName("v_minimum_purchase_amount")
    var vMinimumPurchaseAmount: String? = null,
    @SerializedName("v_price")
    var vPrice: String? = null,
    @SerializedName("v_sponser_image")
    var vSponserImage: String? = null,
    @SerializedName("v_title")
    var vTitle: String? = null,
    @SerializedName("v_validity")
    var vValidity: String? = null
) : Parcelable {
    override fun toString(): String {
        return "$vDiscount%"
    }
}