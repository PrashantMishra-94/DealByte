package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class OpenTime(
    @SerializedName("Friday")
    var friday: String? = null,
    @SerializedName("Monday")
    var monday: String? = null,
    @SerializedName("Saturday")
    var saturday: String? = null,
    @SerializedName("Sunday")
    var sunday: String? = null,
    @SerializedName("Thursday")
    var thursday: String? = null,
    @SerializedName("Tuesday")
    var tuesday: String? = null,
    @SerializedName("Wednesday")
    var wednesday: String? = null
)