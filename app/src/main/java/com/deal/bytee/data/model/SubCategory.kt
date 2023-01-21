package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class SubCategory(
    @SerializedName("sub_category_id")
    var subCategoryId: Int? = null,
    @SerializedName("sub_category_image")
    var subCategoryImage: String? = null,
    @SerializedName("sub_category_name")
    var subCategoryName: String? = null,
    @SerializedName("sub_category_price")
    var subCategoryPrice: String? = null
)