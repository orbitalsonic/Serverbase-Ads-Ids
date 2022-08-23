package com.orbitalsonic.adsserverbase.adsconfig.callback

interface FbBannerCallBack {
    fun onError(adError:String)
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
}