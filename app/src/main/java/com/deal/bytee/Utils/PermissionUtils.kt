package com.deal.bytee.Utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.deal.bytee.R
import com.deal.bytee.fragment.HomeFragment

/**
 * Created by prashant.mishra on 5/5/2018.
 */

class PermissionUtils(private val fragment: HomeFragment, private val method: (()->Unit)? = null) {

    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION/*,
            Manifest.permission.READ_PHONE_STATE*/)
    else arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val allNeededPermissions: Array<String?> get() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in requiredPermissions) {
            permission.let {
                if (!isPermissionGranted(fragment.requireContext(), it)) {
                    allNeededPermissions.add(it)
                }
            }
        }
        return allNeededPermissions.toTypedArray()
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            permission.let {
                if (!isPermissionGranted(fragment.requireContext(), it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        if (!allNeededPermissions.isEmpty()) {
            fragment.requestPermissions(allNeededPermissions, PERMISSION_REQUEST_CODE)
        }
    }

    fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !allPermissionsGranted()) {
            getRuntimePermissions()
        } else {
            method?.invoke()
        }
    }

    fun onRequestPermissionsResult() {
        if (allPermissionsGranted()) {
            method?.invoke()
        } else {
            permissionErrorDialogue()
        }
    }

    private fun permissionErrorDialogue() {
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setCancelable(false)
        builder.setTitle(R.string.request_permission_title)
        builder.setMessage(R.string.request_permission_message)
        builder.setPositiveButton(R.string.close) { dialog, _ ->
            dialog.dismiss()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.finishAffinity(fragment.requireActivity())
            }
        }
        builder.create().show()

    }


    companion object {
        const val TAG = "PermissionUtils"
        const val PERMISSION_REQUEST_CODE = 100

        fun isPermissionGranted(context: Context, permission: String): Boolean {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted: $permission")
                return true
            }
            Log.i(TAG, "Permission NOT granted: $permission")
            return false
        }
    }
}
