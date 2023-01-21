package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class AddReviewRequest(
    @SerializedName("businessid")
    var businessid: String? = null,
    @SerializedName("rating")
    var rating: String? = null,
    @SerializedName("review")
    var review: String? = null,
    @SerializedName("userid")
    var userid: String? = null
)