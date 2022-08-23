package com.orbitalsonic.adsserverbase.adsconfig

import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.orbitalsonic.adsserverbase.adsconfig.callback.FbInterstitialOnCallBack
import com.orbitalsonic.adsserverbase.helpers.datamodel.Ads
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils.AD_TAG

class FbInterstitialAds(activity: Activity) {
    var mInterstitialAd: InterstitialAd? = null
    private val mActivity: Activity = activity
    var isLoadingAd = false

    fun loadInterstitialAd(
        ads: Ads,
        fbInterstitialOnCallBack: FbInterstitialOnCallBack
    ) {
        if (ads.isActive == true && ads.fbAdId !=null) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    ads.fbAdId
                )
                mInterstitialAd?.let{
                    it.loadAd(
                        it.buildLoadAdConfig()
                            .withAdListener(object : InterstitialAdListener {
                                override fun onInterstitialDisplayed(ad: Ad) {
                                    // Interstitial ad displayed callback
                                    fbInterstitialOnCallBack.onInterstitialDisplayed()
                                    Log.i(AD_TAG, "FB Interstitial ad displayed.")
                                }

                                override fun onInterstitialDismissed(ad: Ad) {
                                    // Interstitial dismissed callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onInterstitialDismissed()
                                    Log.i(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd = null
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    mInterstitialAd = null
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onError()
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onAdLoaded()
                                    Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                }

                                override fun onAdClicked(ad: Ad) {
                                    // Ad clicked callback
                                    fbInterstitialOnCallBack.onAdClicked()
                                    Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                }

                                override fun onLoggingImpression(ad: Ad) {
                                    // Ad impression Logged callback
                                    fbInterstitialOnCallBack.onLoggingImpression()
                                    Log.d(AD_TAG, "FB Interstitial ad impression Logged!")
                                }
                            })
                            .build()
                    )
                }

            }
        }else{
            fbInterstitialOnCallBack.onError()
        }

    }

    fun loadShowLoadInterstitialAd(
        ads: Ads,
        fbInterstitialOnCallBack: FbInterstitialOnCallBack
    ) {
        if (ads.isActive == true && ads.fbAdId !=null) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    ads.fbAdId
                )
                mInterstitialAd?.let{
                    it.loadAd(
                        it.buildLoadAdConfig()
                            .withAdListener(object : InterstitialAdListener {
                                override fun onInterstitialDisplayed(ad: Ad) {
                                    // Interstitial ad displayed callback
                                    fbInterstitialOnCallBack.onInterstitialDisplayed()
                                    Log.i(AD_TAG, "FB Interstitial ad displayed.")
                                }

                                override fun onInterstitialDismissed(ad: Ad) {
                                    // Interstitial dismissed callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onInterstitialDismissed()
                                    Log.i(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd?.loadAd()
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    mInterstitialAd = null
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onError()
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onAdLoaded()
                                    Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                }

                                override fun onAdClicked(ad: Ad) {
                                    // Ad clicked callback
                                    fbInterstitialOnCallBack.onAdClicked()
                                    Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                }

                                override fun onLoggingImpression(ad: Ad) {
                                    // Ad impression Logged callback
                                    fbInterstitialOnCallBack.onLoggingImpression()
                                    Log.d(AD_TAG, "FB Interstitial ad impression Logged!")
                                }
                            })
                            .build()
                    )
                }

            }
        }else{
            fbInterstitialOnCallBack.onError()
        }

    }

    fun destroyInterstitialAds(){
        isLoadingAd = false
        mInterstitialAd?.destroy()
        mInterstitialAd = null
    }

    fun showInterstitialAds(){
        if (isInterstitialAdsLoaded()){
            mInterstitialAd?.show()
        }
    }

    fun isInterstitialAdsLoaded():Boolean{
        return mInterstitialAd?.isAdLoaded ?:false
    }


}