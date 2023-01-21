package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class Facility(
    @SerializedName("fac_id")
    var facId: String? = null,
    @SerializedName("fac_title")
    var facTitle: String? = null,
    @SerializedName("image")
    var image: String? = null
)