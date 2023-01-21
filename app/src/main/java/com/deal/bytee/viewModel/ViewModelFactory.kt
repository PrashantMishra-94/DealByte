package com.deal.bytee.viewModel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.deal.bytee.data.api.RetrofitBuilder
import com.deal.bytee.data.repository.Repository
import com.deal.bytee.fragment.VouchersViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor (private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            return DashBoardViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BusinessDetailsViewModel::class.java)) {
            return BusinessDetailsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BuyVoucherViewModel::class.java)) {
            return BuyVoucherViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(VouchersViewModel::class.java)) {
            return VouchersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

    companion object {
        private val factory = ViewModelFactory(Repository(RetrofitBuilder.apiService))

        fun <T : ViewModel?> get(activity: FragmentActivity, modelClass: Class<T>): T {
            return ViewModelProviders.of(activity, factory).get(modelClass)
        }

        fun <T : ViewModel?> get(fragment: Fragment, modelClass: Class<T>): T {
            return ViewModelProviders.of(fragment, factory).get(modelClass)
        }
    }

}