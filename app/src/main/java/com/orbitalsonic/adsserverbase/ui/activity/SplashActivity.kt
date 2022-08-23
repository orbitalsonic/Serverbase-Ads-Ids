package com.orbitalsonic.adsserverbase.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.orbitalsonic.adsserverbase.R
import com.orbitalsonic.adsserverbase.adsconfig.AdmobBannerAds
import com.orbitalsonic.adsserverbase.adsconfig.FbBannerAds
import com.orbitalsonic.adsserverbase.adsconfig.callback.BannerCallBack
import com.orbitalsonic.adsserverbase.adsconfig.callback.FbNativeCallBack
import com.orbitalsonic.adsserverbase.databinding.ActivitySplashBinding
import com.orbitalsonic.adsserverbase.helpers.listeners.OnClickListeners.setSafeOnClickListener


class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var fbBannerAds: FbBannerAds
    private lateinit var admobBannerAds: AdmobBannerAds

    private val mHandler = Handler(Looper.getMainLooper())
    private val adsRunner = Runnable { checkAdvertisement() }

    private var isNativeLoadedOrFailed = false
    private var mCounter: Int = 0

    private lateinit var remoteConfig: FirebaseRemoteConfig

    companion object{
        const val ADS_SERVING = "ads_serving"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding.btnNext.setSafeOnClickListener {
            intentMethod()
            binding.btnNext.isEnabled = false
        }


        if (diComponent.internetHandler.isInternetConnected) {
            initRemoteConfig()
        } else {
            binding.adsContainerLayout.visibility = View.GONE
            isNativeLoadedOrFailed = true
        }

        initAds()

    }

    private fun initAds(){
        fbBannerAds = FbBannerAds(this)
        admobBannerAds = AdmobBannerAds(this)
    }

    private fun loadAds() {
        diComponent.adsViewModel.getAdsObject("largeNative")?.let { mAds ->
            when(mAds.priority?:0){

                1 -> {
                    admobBannerAds.loadNativeAds(binding.adsContainerLayout,
                        binding.admobPlaceHolder,
                        binding.loadingLayout,
                        mAds,
                        2,
                        object : BannerCallBack {

                            override fun onAdFailedToLoad(adError: String) {
                                isNativeLoadedOrFailed = true
                            }

                            override fun onAdLoaded() {
                                isNativeLoadedOrFailed = true
                            }

                            override fun onAdImpression() {
                            }

                        })
                }

                2 -> {
                    fbBannerAds.loadNativeMedium(binding.adsContainerLayout,
                        binding.fbPlaceHolder,
                        binding.loadingLayout,
                        mAds,
                        object : FbNativeCallBack {
                            override fun onError(adError: String) {
                                isNativeLoadedOrFailed = true
                            }

                            override fun onAdLoaded() {
                                isNativeLoadedOrFailed = true
                            }

                            override fun onAdClicked() {}

                            override fun onLoggingImpression() {
                            }

                            override fun onMediaDownloaded() {}

                        })
                }

                 else -> {
                isNativeLoadedOrFailed = true
                binding.adsContainerLayout.visibility = View.GONE
            }
            }
        }?:run{
            isNativeLoadedOrFailed = true
            binding.adsContainerLayout.visibility = View.GONE
        }

    }

    private fun intentMethod() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 2
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchRemoteValues()
    }

    private fun fetchRemoteValues() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            updateRemoteValues()
        }

    }

    private fun checkAdvertisement() {

        if (mCounter < 12) {
            try {
                mCounter++
                if (isNativeLoadedOrFailed) {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.loadingProgress.visibility = View.GONE
                }

            } catch (e: Exception) {

            }

            mHandler.removeCallbacks { adsRunner }
            mHandler.postDelayed(
                adsRunner,
                (1000)
            )
        } else {
            binding.btnNext.visibility = View.VISIBLE
            binding.loadingProgress.visibility = View.GONE
        }

    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(adsRunner)
    }

    override fun onResume() {
        super.onResume()
        mHandler.post(adsRunner)
    }

    private fun updateRemoteValues() {
        diComponent.adsViewModel.setResponseAds(remoteConfig[ADS_SERVING].asString()){

            if (diComponent.internetHandler.isInternetConnected && !diComponent.sharedPrefsUtils.isAppPurchased){
                loadAds()
                mCounter = 0
            }else{
                isNativeLoadedOrFailed = true
            }


            Log.i("RemoteConfig","${diComponent.adsViewModel.adsResponse?.adsServing}")
            Log.i("RemoteConfig","${diComponent.adsViewModel.adsResponse?.ads?.get(0)?.admobAdId}")
            Log.i("RemoteConfig","${diComponent.adsViewModel.adsResponse?.ads?.get(0)?.fbAdId}")
        }
    }

}