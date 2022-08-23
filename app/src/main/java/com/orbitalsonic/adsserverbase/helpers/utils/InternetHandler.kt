package com.orbitalsonic.adsserverbase.helpers.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetHandler(private val connectivityManager: ConnectivityManager) {
    private val tagInternet:String = "internetTag"
    val isInternetConnected: Boolean
        get() {
            try {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } catch (e: Exception) {

            }
            return false
        }
}