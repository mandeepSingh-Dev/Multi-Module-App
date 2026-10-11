package com.app.core_common.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

class CheckInternetConnectivity {
    companion object {
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetwork!=null
        }
    }

}