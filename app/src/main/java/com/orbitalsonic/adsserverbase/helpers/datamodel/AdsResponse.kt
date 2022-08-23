package com.orbitalsonic.adsserverbase.helpers.datamodel

import com.google.gson.annotations.SerializedName


data class AdsResponse (

  @SerializedName("admobAppId" ) var admobAppId : String?        = null,
  @SerializedName("adsServing" ) var adsServing : Boolean?       = null,
  @SerializedName("extras"     ) var extras     : String?        = null,
  @SerializedName("ads"        ) var ads        : ArrayList<Ads> = arrayListOf()

)