package com.orbitalsonic.adsserverbase.adsconfig.callback

interface FbNativeCallBack {
    fun onError(adError:String)
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
    fun onMediaDownloaded()
}