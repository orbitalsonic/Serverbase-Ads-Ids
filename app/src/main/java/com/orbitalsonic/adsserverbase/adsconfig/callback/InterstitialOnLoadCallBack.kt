package com.orbitalsonic.adsserverbase.adsconfig.callback

interface InterstitialOnLoadCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
}