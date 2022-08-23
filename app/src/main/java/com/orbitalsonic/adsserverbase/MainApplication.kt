package com.orbitalsonic.adsserverbase

import android.app.Application
import android.net.ConnectivityManager
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.orbitalsonic.adsserverbase.helpers.utils.InternetHandler
import com.orbitalsonic.adsserverbase.helpers.utils.SharedPrefsUtils
import com.orbitalsonic.adsserverbase.helpers.viewmodel.AdsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
        AdSettings.addTestDevice("3eae0ad7-98ab-4aef-978a-6ba0432c404a")

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(appModule,viewModelModule))
        }
    }

    private val appModule = module{

        single { SharedPrefsUtils(getSharedPreferences("AppSharedPrefs", MODE_PRIVATE)) }
        single { InternetHandler( getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager) }
    }

    private val viewModelModule = module {
        single { AdsViewModel(this@MainApplication) }
    }


}