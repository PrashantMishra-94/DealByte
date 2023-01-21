package com.deal.bytee.data.model


import com.google.gson.annotations.SerializedName

data class DetailedData(
    @SerializedName("area")
    var area: String? = null,
    @SerializedName("bronzemenu")
    var bronzemenu: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("delivery_type")
    var deliveryType: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("discount")
    var discount: String? = null,
    @SerializedName("established_year")
    var establishedYear: String? = null,
    @SerializedName("facebook")
    var facebook: String? = null,
    @SerializedName("goldmenu")
    var goldmenu: String? = null,
    @SerializedName("google_map_url")
    var googleMapUrl: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("instagram")
    var instagram: String? = null,
    @SerializedName("lat")
    var lat: String? = null,
    @SerializedName("long")
    var long: String? = null,
    @SerializedName("main_category")
    var mainCategory: String? = null,
    @SerializedName("open_time")
    var openTime: List<OpenTime?>? = null,
    @SerializedName("rate")
    var rate: String? = null,
    @SerializedName("review_count")
    var reviewCount: String? = null,
    @SerializedName("silvermenu")
    var silvermenu: String? = null,
    @SerializedName("sub_category")
    var subCategory: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("total_employees")
    var totalEmployees: String? = null,
    @SerializedName("twitter")
    var twitter: String? = null
)