package com.orbitalsonic.adsserverbase.adsconfig.callback

interface FbInterstitialOnCallBack {
    fun onInterstitialDisplayed()
    fun onInterstitialDismissed()
    fun onError()
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
}