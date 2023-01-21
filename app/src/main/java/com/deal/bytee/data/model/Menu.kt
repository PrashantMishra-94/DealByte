package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("category_id")
    var categoryId: Int? = null,
    @SerializedName("category_image")
    var categoryImage: String? = null,
    @SerializedName("category_name")
    var categoryName: String? = null,
    @SerializedName("sub_category")
    var subCategory: List<SubCategory?>? = null,
    @SerializedName("total_items")
    var totalItems: String? = null
)