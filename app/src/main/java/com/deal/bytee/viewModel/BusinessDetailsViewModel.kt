package com.deal.bytee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deal.bytee.Utils.Resource
import com.deal.bytee.data.model.AddReviewRequest
import com.deal.bytee.data.model.BusinessDetails
import com.deal.bytee.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class BusinessDetailsViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var businessDetails: LiveData<Resource<BusinessDetails>>

    fun getBusinessDetails(userId: String, businessId: String) : LiveData<Resource<BusinessDetails>> {
        if (!this::businessDetails.isInitialized) {
            businessDetails = repository.getBusinessDetails(userId, businessId, viewModelScope.coroutineContext + Dispatchers.IO)
        }
        return businessDetails
    }

    fun refreshBusinessDetails(userId: String, businessId: String): LiveData<Resource<BusinessDetails>> {
        businessDetails = repository.getBusinessDetails(userId, businessId, viewModelScope.coroutineContext + Dispatchers.IO)
        return businessDetails
    }

    fun addToWishList(userId: String, businessId: String) = repository.addToWishlist(
        userId, businessId, viewModelScope.coroutineContext + Dispatchers.IO
    )

    fun addReview(userId: String, businessId: String, rating: Float, review: String) = repository.addReview(
        AddReviewRequest(
            userid = userId,
            businessid = businessId,
            rating = rating.toString(),
            review = review
        ), viewModelScope.coroutineContext + Dispatchers.IO
    )
}