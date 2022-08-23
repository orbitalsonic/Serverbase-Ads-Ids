package com.orbitalsonic.adsserverbase.adsconfig

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.orbitalsonic.adsserverbase.R
import com.orbitalsonic.adsserverbase.adsconfig.callback.BannerCallBack
import com.orbitalsonic.adsserverbase.helpers.datamodel.Ads
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils.AD_TAG

class AdmobBannerAds(activity: Activity) {

    private var adaptiveAdView: AdView? = null
    private var adMobNativeAd: NativeAd? = null
    private val mActivity: Activity = activity
    private var adLoader: AdLoader? = null

    fun loadBannerAds(
        adsContainerLayout: LinearLayout,
        adsHolder: LinearLayout,
        loadingFrameLayout: FrameLayout,
        ads: Ads,
        bannerCallBack: BannerCallBack
    ) {
        if (ads.isActive == true && ads.admobAdId !=null) {
            adaptiveAdView = AdView(mActivity)
            adsHolder.addView(adaptiveAdView)
            adaptiveAdView?.adUnitId = ads.admobAdId!!
            adaptiveAdView?.adSize = getAdSize(adsHolder)

            val adRequest = AdRequest
                .Builder()
                .build()
            adaptiveAdView?.loadAd(adRequest)
            adaptiveAdView?.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    Log.i(AD_TAG, "admob banner onAdLoaded")
                    loadingFrameLayout.visibility = View.GONE
                    adsHolder.visibility = View.VISIBLE
                    bannerCallBack.onAdLoaded()
                }

                override fun onAdFailedToLoad(adError : LoadAdError) {
                    Log.e(AD_TAG, "admob banner onAdFailedToLoad: ${adError.message}")
                    adsContainerLayout.visibility = View.GONE
                    bannerCallBack.onAdFailedToLoad(adError.message)
                }

                override fun onAdImpression() {
                    Log.i(AD_TAG, "admob banner onAdImpression")
                    bannerCallBack.onAdImpression()
                    super.onAdImpression()
                }
            }
        }else{
            adsContainerLayout.visibility = View.GONE
            bannerCallBack.onAdFailedToLoad("Something happened")
        }

    }

    fun loadNativeAds(
        adsContainerLayout: LinearLayout,
        adsHolder: FrameLayout,
        loadingFrameLayout: FrameLayout,
        ads: Ads,
        nativeNo: Int,
        bannerCallBack: BannerCallBack
    ) {
        if (ads.isActive == true && ads.admobAdId !=null) {
            val builder: AdLoader.Builder = AdLoader.Builder(mActivity, ads.admobAdId!!)
            adLoader =
                builder.forNativeAd { unifiedNativeAd: NativeAd? -> adMobNativeAd = unifiedNativeAd }
                    .withAdListener(object : AdListener() {
                        override fun onAdImpression() {
                            super.onAdImpression()
                            Log.i(AD_TAG, "admob native onAdImpression")
                            bannerCallBack.onAdImpression()
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.e(AD_TAG, "admob native onAdFailedToLoad: " + loadAdError.message)
                            bannerCallBack.onAdFailedToLoad(loadAdError.message)
                            adsContainerLayout.visibility = View.GONE
                            super.onAdFailedToLoad(loadAdError)
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            Log.i(AD_TAG, "admob native onAdLoaded")
                            bannerCallBack.onAdLoaded()
                            loadingFrameLayout.visibility = View.GONE
                            adsHolder.visibility = View.VISIBLE
                            populateUnifiedNativeAdView(adsHolder,nativeNo)

                        }

                    }).withNativeAdOptions(
                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                            .setAdChoicesPlacement(
                                NativeAdOptions.ADCHOICES_TOP_RIGHT
                            ).build()
                    )
                    .build()
            adLoader?.loadAd(AdRequest.Builder().build())
        }else{
            bannerCallBack.onAdFailedToLoad("Something happened")
            adsContainerLayout.visibility = View.GONE
        }
    }

    private fun getAdSize(adContainer: LinearLayout): AdSize {
        val display = mActivity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
    }

    fun populateUnifiedNativeAdView(
        adMobNativeContainer: FrameLayout,
        nativeNo: Int
    ) {

        adMobNativeAd?.let { ad ->
            val inflater = LayoutInflater.from(mActivity)
            val adView: NativeAdView = if (nativeNo == 1) {
                inflater.inflate(R.layout.admob_native_small, null) as NativeAdView
            } else {
                inflater.inflate(R.layout.admob_native_medium, null) as NativeAdView
            }
            adMobNativeContainer.visibility = View.VISIBLE
            adMobNativeContainer.removeAllViews()
            adMobNativeContainer.addView(adView)

            if (nativeNo == 2) {
                val mediaView: MediaView = adView.findViewById(R.id.media_view)
                adView.mediaView = mediaView
            }


            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)

            //Headline
            adView.headlineView?.let { headline ->
                (headline as TextView).text = ad.headline
                headline.isSelected = true
            }

            //Body
            adView.bodyView?.let { bodyView ->
                if (ad.body == null) {
                    bodyView.visibility = View.INVISIBLE
                } else {
                    bodyView.visibility = View.VISIBLE
                    (bodyView as TextView).text = ad.body
                }

            }

            //Call to Action
            adView.callToActionView?.let { ctaView ->
                if (ad.callToAction == null) {
                    ctaView.visibility = View.INVISIBLE
                } else {
                    ctaView.visibility = View.VISIBLE
                    (ctaView as Button).text = ad.callToAction
                }

            }

            //Icon
            adView.iconView?.let { iconView ->
                if (ad.icon == null) {
                    iconView.visibility = View.GONE
                } else {
                    (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                    iconView.visibility = View.VISIBLE
                }

            }

            adView.advertiserView?.let { adverView ->

                if (ad.advertiser == null) {
                    adverView.visibility = View.GONE
                } else {
                    (adverView as TextView).text = ad.advertiser
                    adverView.visibility = View.GONE
                }
            }

            adView.setNativeAd(ad)
        }
    }

}