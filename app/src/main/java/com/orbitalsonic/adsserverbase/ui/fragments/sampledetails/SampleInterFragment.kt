package com.orbitalsonic.adsserverbase.ui.fragments.sampledetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orbitalsonic.adsserverbase.R
import com.orbitalsonic.adsserverbase.adsconfig.AdmobInterstitialAds
import com.orbitalsonic.adsserverbase.adsconfig.FbInterstitialAds
import com.orbitalsonic.adsserverbase.adsconfig.callback.*
import com.orbitalsonic.adsserverbase.databinding.FragmentSampleInterBinding
import com.orbitalsonic.adsserverbase.helpers.listeners.OnClickListeners.setSafeOnClickListener
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils
import com.orbitalsonic.adsserverbase.ui.fragments.BaseFragment

class SampleInterFragment : BaseFragment<FragmentSampleInterBinding>() {

    private lateinit var admobInterstitialAds: AdmobInterstitialAds
    private lateinit var fbInterstitialAds: FbInterstitialAds

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_sample_inter)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true

            binding.btnShow.setSafeOnClickListener {
                showInterstitialAd()
            }

            adsConfig()
        }
    }

    private fun adsConfig(){
        activity?.let { mActivity ->
            admobInterstitialAds = AdmobInterstitialAds(mActivity)
            fbInterstitialAds = FbInterstitialAds(mActivity)

            if (diComponent.internetHandler.isInternetConnected && !diComponent.sharedPrefsUtils.isAppPurchased){
                loadInterstitialAd()
            }

        }

    }

    private fun loadInterstitialAd(){
        activity?.let { mActivity ->

            diComponent.adsViewModel.getAdsObject("allInterstitial")?.let { mAds ->
                when(mAds.priority?:0){
                    1 -> {
                        Log.d(GeneralUtils.AD_TAG, "Call Admob Interstitial")
                        admobInterstitialAds.loadInterstitialAd(
                            mAds,
                            object : InterstitialOnLoadCallBack {
                                override fun onAdFailedToLoad(adError: String) {
                                }

                                override fun onAdLoaded() {
                                }

                            })
                    }
                    2 -> {
                        Log.d(GeneralUtils.AD_TAG, "Call FB Interstitial")
                        fbInterstitialAds.loadInterstitialAd(
                            mAds,
                            object : FbInterstitialOnCallBack {
                                override fun onInterstitialDisplayed() {}

                                override fun onInterstitialDismissed() {}

                                override fun onError() {}

                                override fun onAdLoaded() {}

                                override fun onAdClicked() {}

                                override fun onLoggingImpression() {}

                            })
                    }

                }
            }

        }
    }

    private fun showInterstitialAd(){
        diComponent.adsViewModel.getAdsObject("allInterstitial")?.let { mAds ->
            when(mAds.priority?:0){
                1 -> {
                    admobInterstitialAds.showInterstitialAd(object :
                        InterstitialOnShowCallBack {
                        override fun onAdDismissedFullScreenContent() {
                        }

                        override fun onAdFailedToShowFullScreenContent() {
                        }

                        override fun onAdShowedFullScreenContent() {
                        }

                        override fun onAdImpression() {
                        }

                    })
                }
                2 -> {
                    fbInterstitialAds.showInterstitialAds()
                }

            }
        }
    }


}