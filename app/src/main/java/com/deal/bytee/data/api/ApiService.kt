package com.deal.bytee.data.api

import com.deal.bytee.Utils.Endpoints
import com.deal.bytee.data.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(Endpoints.FETCH_PROFILE_API)
    suspend fun getProfileDetails(@Body user: UserRequest) : ProfileDetails

    @POST(Endpoints.GET_BUSINESS_DETAILS)
    suspend fun getBusinessDetails(@Body businessDetailsRequest: BusinessDetailsRequest) : BusinessDetails

    @POST(Endpoints.ADD_WISHLIST)
    suspend fun addToWishlist(@Body businessDetailsRequest: BusinessDetailsRequest): GeneralResponse

    @POST(Endpoints.ADD_RATE_REVIEW)
    suspend fun addReview(@Body addReviewRequest: AddReviewRequest): AddReviewResponse

    @POST(Endpoints.BUY_VOUCHER)
    suspend fun buyVoucher(@Body buyVoucherRequest: BuyVoucherRequest): BuyVoucherResponse

    @POST(Endpoints.REDEEM_VOUCHER)
    suspend fun applyVoucher(@Body applyVoucherRequest: ApplyVoucherRequest): ApplyVoucherResponse

    @POST(Endpoints.GET_MY_VOUCHERS_LIST)
    suspend fun getMyVoucherList(@Body myVoucherRequest: MyVoucherRequest): MyVoucherResponse
}