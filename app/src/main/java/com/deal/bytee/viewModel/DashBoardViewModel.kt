package com.deal.bytee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deal.bytee.Utils.Resource
import com.deal.bytee.data.model.ProfileDetails
import com.deal.bytee.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class DashBoardViewModel(private val repository: Repository): ViewModel() {

    private lateinit var profileDetails: LiveData<Resource<ProfileDetails>>

    fun getProfileDetails(userId: String): LiveData<Resource<ProfileDetails>> {
        if (!this::profileDetails.isInitialized) {
            profileDetails = repository.getProfileDetails(userId, viewModelScope.coroutineContext + Dispatchers.IO)
        }
        return profileDetails
    }

}