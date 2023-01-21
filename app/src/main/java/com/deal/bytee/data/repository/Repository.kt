package com.deal.bytee.data.repository

import androidx.lifecycle.liveData
import com.deal.bytee.Utils.Resource
import com.deal.bytee.data.api.ApiService
import com.deal.bytee.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class Repository(private val apiService: ApiService) {

    fun getProfileDetails(userId: String, context: CoroutineContext = Dispatchers.IO) =  liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.getProfileDetails(UserRequest(userId))))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun getBusinessDetails(userId: String, businessId: String, context: CoroutineContext = Dispatchers.IO) =  liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.getBusinessDetails(BusinessDetailsRequest(userid = userId, businessid = businessId))))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun addToWishlist(userId: String, businessId: String, context: CoroutineContext = Dispatchers.IO) =  liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.addToWishlist(BusinessDetailsRequest(userid = userId, businessid = businessId))))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun addReview(addReviewRequest: AddReviewRequest, context: CoroutineContext = Dispatchers.IO) =  liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.addReview(addReviewRequest)))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun buyVoucher(buyVoucherRequest: BuyVoucherRequest, context: CoroutineContext = Dispatchers.IO) =  liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.buyVoucher(buyVoucherRequest)))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun applyVoucher(applyVoucherRequest: ApplyVoucherRequest, context: CoroutineContext = Dispatchers.IO) = liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.applyVoucher(applyVoucherRequest)))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

    fun getMyVoucherList(myVoucherRequest: MyVoucherRequest, context: CoroutineContext = Dispatchers.IO) = liveData(context) {
        emit(Resource.loading())
        try {
            emit(Resource.success(data = apiService.getMyVoucherList(myVoucherRequest)))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message ?: "Error Occurred"))
        }
    }

}