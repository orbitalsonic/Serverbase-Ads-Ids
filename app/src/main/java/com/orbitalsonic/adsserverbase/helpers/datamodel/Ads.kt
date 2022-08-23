package com.orbitalsonic.adsserverbase.helpers.datamodel

import com.google.gson.annotations.SerializedName


data class Ads (
  @SerializedName("adsType"   ) var adsType   : String?  = null,
  @SerializedName("priority"  ) var priority  : Int?     = null,
  @SerializedName("isActive"  ) var isActive  : Boolean? = null,
  @SerializedName("admobAdId" ) var admobAdId : String?  = null,
  @SerializedName("fbAdId"    ) var fbAdId    : String?  = null
)