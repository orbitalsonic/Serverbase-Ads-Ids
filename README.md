# Serverbase-Ads
Send Ad ids using Firebase RemoteConfig

###### Firebase RemoteConfig Key

```
 ads_serving
 
 ```

###### Add following json code in FirebaseRemoteConfig

```
{
  "admobAppId": "ca-app-pub-3940256099942544~3347511713",
  "adsServing": true,
  "extras": "empty", 
  "ads": [
    {
      "adsType": "simpleBanner",
      "priority": 1,
      "isActive": true,
      "admobAdId": "ca-app-pub-3940256099942544/6300978111",
      "fbAdId": "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    },
    {
      "adsType": "smallNative",
      "priority": 1,
      "isActive": true,
      "admobAdId": "ca-app-pub-3940256099942544/2247696110",
      "fbAdId": "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    },
    {
      "adsType": "largeNative",
      "priority": 1,
      "isActive": true,
      "admobAdId": "ca-app-pub-3940256099942544/2247696110",
      "fbAdId": "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    },
    {
      "adsType": "allInterstitial",
      "priority": 1,
      "isActive": true,
      "admobAdId": "ca-app-pub-3940256099942544/1033173712",
      "fbAdId": "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    }
  ]
}
 
 ```
