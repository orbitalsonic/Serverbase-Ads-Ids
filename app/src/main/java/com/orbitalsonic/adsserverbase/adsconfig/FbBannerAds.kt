package com.orbitalsonic.adsserverbase.adsconfig

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.orbitalsonic.adsserverbase.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbitalsonic.adsserverbase.adsconfig.callback.FbBannerCallBack
import com.orbitalsonic.adsserverbase.adsconfig.callback.FbNativeCallBack
import com.facebook.ads.*
import com.orbitalsonic.adsserverbase.helpers.datamodel.Ads
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils.AD_TAG

class FbBannerAds(activity: Activity) {

    private var adViewBanner: AdView? = null
    private val mActivity: Activity = activity
    private var nativeMedium: NativeAd? = null
    private var nativeSmall: NativeBannerAd? = null
    

    fun loadBannerAds(
        adsContainerLayout: LinearLayout,
        adsHolder: LinearLayout,
        loadingFrameLayout: FrameLayout,
        ads: Ads,
        fbBannerCallBack: FbBannerCallBack
    ) {
        if (ads.isActive == true && ads.fbAdId !=null) {
            adViewBanner = AdView(mActivity, ads.fbAdId, AdSize.BANNER_HEIGHT_90)
            adViewBanner?.let {
                adsHolder.addView(it)
                it.loadAd()
                it.loadAd(it.buildLoadAdConfig().withAdListener(object : AdListener {

                    override fun onError(p0: Ad?, p1: AdError?) {
                        Log.e(AD_TAG, "FB Banner onError: ${p1?.errorMessage}")
                        fbBannerCallBack.onError(p1?.errorMessage.toString())
                        adsContainerLayout.visibility = View.GONE
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onAdLoaded")
                        loadingFrameLayout.visibility = View.GONE
                        adsHolder.visibility = View.VISIBLE
                        fbBannerCallBack.onAdLoaded()
                    }

                    override fun onAdClicked(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onAdClicked")
                        fbBannerCallBack.onAdClicked()
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Log.i(AD_TAG, "FB Banner onLoggingImpression")
                        fbBannerCallBack.onLoggingImpression()
                    }

                }).build())

            }

        } else {
            adsContainerLayout.visibility = View.GONE
            fbBannerCallBack.onError("Something happened")
        }

    }

    fun destroyBannerAds() {
        adViewBanner?.destroy()
    }

    fun loadNativeMedium(
        adsContainerLayout: LinearLayout,
        nativeAdLayout: NativeAdLayout,
        loadingFrameLayout: FrameLayout,
        ads: Ads,
        fbNativeCallBack: FbNativeCallBack
    ) {

        if (ads.isActive == true && ads.fbAdId !=null) {
            nativeMedium = NativeAd(mActivity, ads.fbAdId)
            nativeMedium?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                fbNativeCallBack.onMediaDownloaded()
                                Log.i(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsContainerLayout.visibility = View.GONE
                                fbNativeCallBack.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                loadingFrameLayout.visibility = View.GONE
                                fbNativeCallBack.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeMedium != ad) {
                                    return
                                }
                                nativeAdLayout.visibility = View.VISIBLE
                                // Inflate Native Ad into Container
                                inflateNativeMedium(it, nativeAdLayout)
                            }

                            override fun onAdClicked(ad: Ad) {
                                fbNativeCallBack.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                fbNativeCallBack.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsContainerLayout.visibility = View.GONE
            fbNativeCallBack.onError("Something Happened")
        }


    }

    fun loadNativeSmallAd(
        adsContainerLayout: LinearLayout,
        nativeAdLayout: NativeAdLayout,
        loadingFrameLayout: FrameLayout,
        ads: Ads,
        fbNativeCallBack: FbNativeCallBack
    ) {

        if (ads.isActive == true && ads.fbAdId !=null) {
            nativeSmall = NativeBannerAd(mActivity, ads.fbAdId)
            nativeSmall?.let {
                it.loadAd(
                    it.buildLoadAdConfig()
                        .withAdListener(object : NativeAdListener {
                            override fun onMediaDownloaded(ad: Ad) {
                                fbNativeCallBack.onMediaDownloaded()
                                Log.d(AD_TAG, "FB Native ad finished downloading all assets.")
                            }

                            override fun onError(ad: Ad, adError: AdError) {
                                adsContainerLayout.visibility = View.GONE
                                fbNativeCallBack.onError(adError.errorMessage)
                                Log.e(AD_TAG, "FB Native ad failed to load: " + adError.errorMessage)
                            }

                            override fun onAdLoaded(ad: Ad) {
                                loadingFrameLayout.visibility = View.GONE
                                fbNativeCallBack.onAdLoaded()
                                Log.d(AD_TAG, "FB Native ad is loaded and ready to be displayed!")
                                if (nativeSmall != ad) {
                                    return
                                }
                                nativeAdLayout.visibility = View.VISIBLE
                                // Inflate Native Ad into Container
                                inflateNativeSmall(it, nativeAdLayout)
                            }

                            override fun onAdClicked(ad: Ad) {
                                fbNativeCallBack.onAdClicked()
                                Log.d(AD_TAG, "FB Native ad clicked!")
                            }

                            override fun onLoggingImpression(ad: Ad) {
                                fbNativeCallBack.onLoggingImpression()
                                Log.d(AD_TAG, "FB Native ad impression logged!")
                            }
                        })
                        .build()
                )
            }

        } else {
            adsContainerLayout.visibility = View.GONE
            fbNativeCallBack.onError("Something Happened")
        }

    }

    private fun inflateNativeMedium(nativeAd: NativeAd, nativeAdLayout: NativeAdLayout) {
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeM: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_medium, nativeAdLayout, false) as ConstraintLayout
        nativeAdLayout.addView(adViewNativeM)
        // Add the AdOptionsView
        val adChoicesContainer: LinearLayout = adViewNativeM.findViewById(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(mActivity, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon: MediaView = adViewNativeM.findViewById(R.id.native_ad_icon)
        val nativeAdTitle = adViewNativeM.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdMedia: MediaView = adViewNativeM.findViewById(R.id.native_ad_media)
        val nativeAdSocialContext =
            adViewNativeM.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdBody = adViewNativeM.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adViewNativeM.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction: Button = adViewNativeM.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adViewNativeM, nativeAdMedia, nativeAdIcon, clickableViews
        )

    }

    private fun inflateNativeSmall(nativeBannerAd: NativeBannerAd, nativeAdLayout: NativeAdLayout) {
        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        // Add the Ad view into the ad container.
        val inflater = LayoutInflater.from(mActivity)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        val adViewNativeB: ConstraintLayout =
            inflater.inflate(R.layout.fb_native_small, nativeAdLayout, false) as ConstraintLayout
        nativeAdLayout.addView(adViewNativeB)

        // Add the AdChoices icon
        val adChoicesContainer: LinearLayout =
            adViewNativeB.findViewById(R.id.ad_choices_container)
        val adOptionsView =
            AdOptionsView(mActivity, nativeBannerAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdTitle: TextView = adViewNativeB.findViewById(R.id.native_ad_title)
        val nativeAdSocialContext: TextView =
            adViewNativeB.findViewById(R.id.native_ad_social_context)
        val sponsoredLabel: TextView = adViewNativeB.findViewById(R.id.native_ad_sponsored_label)
        val nativeAdIconView: MediaView = adViewNativeB.findViewById(R.id.native_icon_view)
        val nativeAdCallToAction: Button = adViewNativeB.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdCallToAction.text = nativeBannerAd.adCallToAction
        nativeAdCallToAction.visibility =
            if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdTitle.text = nativeBannerAd.advertiserName
        nativeAdSocialContext.text = nativeBannerAd.adSocialContext
        sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

        // Register the Title and CTA button to listen for clicks.
        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeBannerAd.registerViewForInteraction(
            adViewNativeB, nativeAdIconView, clickableViews
        )
    }

}