package com.orbitalsonic.adsserverbase.adsconfig

import android.app.Activity
import android.util.Log
import com.orbitalsonic.adsserverbase.adsconfig.callback.InterstitialOnLoadCallBack
import com.orbitalsonic.adsserverbase.adsconfig.callback.InterstitialOnShowCallBack
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.orbitalsonic.adsserverbase.helpers.datamodel.Ads
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils.AD_TAG

class AdmobInterstitialAds(activity: Activity) {

    private var mActivity: Activity = activity
    private var mInterstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest = AdRequest.Builder().build()

    private var isLoadingAd = false


    fun loadInterstitialAd(
        ads: Ads,
        interstitialOnLoadCallBack: InterstitialOnLoadCallBack
    ) {

        if (ads.isActive == true && ads.admobAdId !=null) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                InterstitialAd.load(
                    mActivity,
                    ads.admobAdId!!,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(AD_TAG, "admob Interstitial onAdFailedToLoad: ${adError.message}")
                            isLoadingAd = false
                            mInterstitialAd = null
                            interstitialOnLoadCallBack.onAdFailedToLoad(adError.toString())
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.i(AD_TAG, "admob Interstitial onAdLoaded")
                            isLoadingAd = false
                            mInterstitialAd = interstitialAd
                            interstitialOnLoadCallBack.onAdLoaded()

                        }
                    })
            }
        }else{
            interstitialOnLoadCallBack.onAdFailedToLoad("Something happened!")
        }
    }

    fun showInterstitialAd( interstitialOnShowCallBack: InterstitialOnShowCallBack) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.i(AD_TAG, "admob Interstitial onAdImpression")
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }
    }

    fun showAndLoadInterstitialAd( ads: Ads, interstitialOnShowCallBack: InterstitialOnShowCallBack) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdDismissedFullScreenContent")
                    interstitialOnShowCallBack.onAdDismissedFullScreenContent()
                    loadAgainInterstitialAd(ads)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(AD_TAG, "admob Interstitial onAdFailedToShowFullScreenContent: ${adError.message}")
                    interstitialOnShowCallBack.onAdFailedToShowFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(AD_TAG, "admob Interstitial onAdShowedFullScreenContent")
                    interstitialOnShowCallBack.onAdShowedFullScreenContent()
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.i(AD_TAG, "admob Interstitial onAdImpression")
                    interstitialOnShowCallBack.onAdImpression()
                }
            }
            mInterstitialAd?.show(mActivity)
        }

    }

    private fun loadAgainInterstitialAd(
        ads: Ads,
    ) {
        if (ads.isActive == true && ads.admobAdId !=null) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                InterstitialAd.load(
                    mActivity,
                    ads.admobAdId!!,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(AD_TAG, "admob Interstitial onAdFailedToLoad: $adError")
                            isLoadingAd = false
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.i(AD_TAG, "admob Interstitial onAdLoaded")
                            isLoadingAd = false
                            mInterstitialAd = interstitialAd

                        }
                    })
            }
        }

    }

    fun isInterstitialLoaded(): Boolean {
        return mInterstitialAd != null
    }

    fun dismissInterstitialLoaded() {
        mInterstitialAd = null
    }

}