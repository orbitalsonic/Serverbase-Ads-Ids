package com.orbitalsonic.adsserverbase.ui.fragments.sampledetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orbitalsonic.adsserverbase.R
import com.orbitalsonic.adsserverbase.adsconfig.AdmobBannerAds
import com.orbitalsonic.adsserverbase.adsconfig.FbBannerAds
import com.orbitalsonic.adsserverbase.adsconfig.callback.BannerCallBack
import com.orbitalsonic.adsserverbase.adsconfig.callback.FbNativeCallBack
import com.orbitalsonic.adsserverbase.databinding.FragmentSampleNativeBinding
import com.orbitalsonic.adsserverbase.helpers.utils.GeneralUtils.AD_TAG
import com.orbitalsonic.adsserverbase.ui.fragments.BaseFragment

class SampleNativeFragment : BaseFragment<FragmentSampleNativeBinding>() {

    private lateinit var fbBannerAds: FbBannerAds
    private lateinit var admobBannerAds: AdmobBannerAds

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_sample_native)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            adsConfig()
        }
    }

    private fun adsConfig() {
        activity?.let { mActivity ->
            fbBannerAds = FbBannerAds(mActivity)
            admobBannerAds = AdmobBannerAds(mActivity)

            if (diComponent.internetHandler.isInternetConnected && !diComponent.sharedPrefsUtils.isAppPurchased){
                loadAds()
            }else{
                binding.adsContainerLayout.visibility = View.GONE
            }

        }

    }

    private fun loadAds(){

        diComponent.adsViewModel.getAdsObject("smallNative")?.let { mAds ->
            when(mAds.priority?:0){
                1 ->{
                    Log.d(AD_TAG, "Call Admob Native")
                    binding.admobNativePlaceHolder.visibility = View.VISIBLE
                    binding.fbNativePlaceHolder.visibility = View.GONE
                    admobBannerAds.loadNativeAds(binding.adsContainerLayout,
                        binding.admobNativePlaceHolder,
                        binding.loadingLayout,
                        mAds,
                        1,
                        object : BannerCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                            }

                            override fun onAdLoaded() {
                            }

                            override fun onAdImpression() {
                            }

                        })
                }
                2 ->{
                    Log.d(AD_TAG, "Call FB Native")
                    binding.admobNativePlaceHolder.visibility = View.GONE
                    binding.fbNativePlaceHolder.visibility = View.VISIBLE
                    fbBannerAds.loadNativeSmallAd(binding.adsContainerLayout,
                        binding.fbNativePlaceHolder,
                        binding.loadingLayout,
                        mAds,
                        object : FbNativeCallBack {
                            override fun onError(adError: String) { }

                            override fun onAdLoaded() {}

                            override fun onAdClicked() {}

                            override fun onLoggingImpression() {}
                            override fun onMediaDownloaded() {}

                        })
                }

                else ->{
                    binding.adsContainerLayout.visibility = View.GONE
                }
            }
        }?:run{
            binding.adsContainerLayout.visibility = View.GONE
        }


    }

}