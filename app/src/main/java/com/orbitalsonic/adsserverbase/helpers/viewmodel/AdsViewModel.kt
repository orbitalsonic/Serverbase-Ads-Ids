package com.orbitalsonic.adsserverbase.helpers.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.orbitalsonic.adsserverbase.helpers.datamodel.Ads
import com.orbitalsonic.adsserverbase.helpers.datamodel.AdsResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException

class AdsViewModel(application: Application) : AndroidViewModel(application) {
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("AdsResponse", "$exception")
    }

    var adsResponse:AdsResponse? = null

    fun setResponseAds(jsonData:String?,callback: () -> Unit) {
        if (adsResponse == null) {
            viewModelScope.launch(Dispatchers.Main + handler) {
                async(Dispatchers.IO + handler) {
                    try {
                        jsonData?.let {
                            val gson = Gson()
                            adsResponse = gson.fromJson(it, AdsResponse::class.java)
                        }

                    } catch (e: JSONException) {

                    }

                }.await()
                callback.invoke()
            }
        }

    }

    fun clearResponse() {
        adsResponse = null
    }

    fun getAdsObject(adsType:String):Ads?{
        adsResponse?.let { mResponse ->
            try {
                if (mResponse.adsServing == true){
                    return mResponse.ads.find { it.adsType == adsType }
                }else{
                    return null
                }
            }catch (e:Exception){
                return null
            }


        }?:run{
            return null
        }
    }


}