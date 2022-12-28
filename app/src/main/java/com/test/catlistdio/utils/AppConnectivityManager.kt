package com.test.catlistdio.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class AppConnectivityManager (appContext: Context) {

    val cm: ConnectivityManager

    init {
        cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * Periksa apakah ponsel memiliki konektivitas internet.
     */
    fun isConnectedToInternet(): Boolean {
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork != null) {
            return activeNetwork?.isConnectedOrConnecting
        } else {
            return false
        }
    }
}