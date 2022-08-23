package com.orbitalsonic.adsserverbase.helpers.utils

import android.content.SharedPreferences


private const val is_app_purchased = "is_app_purchased"


class SharedPrefsUtils (private val mSharedPreferences: SharedPreferences){

    var isAppPurchased: Boolean
        get() = mSharedPreferences.getBoolean(is_app_purchased, false)
        set(value) {
            mSharedPreferences.edit().apply {
                putBoolean(is_app_purchased, value)
                apply()
            }
        }

}