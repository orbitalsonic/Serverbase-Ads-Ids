package com.orbitalsonic.adsserverbase.helpers.di

import com.orbitalsonic.adsserverbase.helpers.utils.InternetHandler
import com.orbitalsonic.adsserverbase.helpers.utils.SharedPrefsUtils
import com.orbitalsonic.adsserverbase.helpers.viewmodel.AdsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent: KoinComponent {

    val sharedPrefsUtils: SharedPrefsUtils by inject()
    val internetHandler: InternetHandler by inject()
    val adsViewModel: AdsViewModel by inject()

}