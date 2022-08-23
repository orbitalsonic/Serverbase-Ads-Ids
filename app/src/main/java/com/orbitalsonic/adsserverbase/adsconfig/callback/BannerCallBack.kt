package com.orbitalsonic.adsserverbase.adsconfig.callback

interface BannerCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdImpression()
}