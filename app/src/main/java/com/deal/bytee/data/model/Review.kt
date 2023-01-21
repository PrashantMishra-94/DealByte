package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("rating")
    var rating: String? = null,
    @SerializedName("review_id")
    var reviewId: Int? = null,
    @SerializedName("review_text")
    var reviewText: String? = null,
    @SerializedName("reviewer_image")
    var reviewerImage: String? = null,
    @SerializedName("reviewer_name")
    var reviewerName: String? = null,
    @SerializedName("time")
    var time: String? = null
)