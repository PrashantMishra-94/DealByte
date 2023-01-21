package com.deal.bytee.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.deal.bytee.R

fun isInternetAvailable(context: Context, shouldShowMessage: Boolean = true): Boolean {
    val connectivity =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivity != null) {
        val info = connectivity.allNetworkInfo
        if (info != null) {
            for (anInfo in info) {
                if (anInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
    }
    if (shouldShowMessage) {
        Toast.makeText(
            context, R.string.internet_con_not_available,
            Toast.LENGTH_SHORT
        ).show()
    }
    return false
}

fun isLogin() = App.sharedPreferences.getKey(MySharedPreferences.isLogin) == MySharedPreferences.YES

fun getUserId() = App.sharedPreferences.getKey(MySharedPreferences.id) ?: ""

fun isAllEmpty(vararg strings: String?): Boolean {
    for (str in strings) {
        if (!str.isNullOrBlank())
            return false
    }
    return true
}