# adsjmsdk

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

## 详细接入文档：
+ [Android 接入文档](http://sdk.hzsanjiaomao.com/sdkdoc/0-SDK-HTML1/md/flutter/Android_flutter.html)
+ [iOS 接入文档](http://sdk.hzsanjiaomao.com/sdk/iOS/iOS%E6%96%87%E6%A1%A3/SJMSDK_Flutter_%E6%96%87%E6%A1%A3.html)

## Initialization SDK
```
SjmAdSdk.init("AppId");
```

## Examples：

### Splash Ad
```
SplashAdView(
    adId: "adId",
    width: width,
    height: height,
    onSjmAdLoaded: (String id) {
        print("SplashAd onSjmAdLoaded");
    },
    onSjmAdShow: (String id) {
        print("SplashAd onSjmAdShow");
    },
    onSjmAdClicked: (String id) {
        print("SplashAd onSjmAdClicked");
    },
    onSjmAdClosed: (String id) {
        print("SplashAd onSjmAdClosed");
    },
    onSjmAdError: (String id, int code, String message) {
        print("SplashAd onSjmAdError");
    }
)
```


### Reward Video Ad
```
SjmAdSdk.showRewardVideoAd(
    "adId",
    "userId",
    onSjmAdTradeId:(String id){
        print("RewardVideoAd onSjmAdTradeId");
    },
    onSjmAdLoaded: (String id) {
        print("RewardVideoAd onSjmAdLoad");
    },
    onSjmAdShow: (String id) {
        print("RewardVideoAd onSjmAdShow");
    },
    onSjmAdReward: (String id) {
        print("RewardVideoAd onReward");
    },
    onSjmAdClicked: (String id) {
        print("RewardVideoAd onSjmAdClick");
    },
    onSjmAdVideoComplete: (String id) {
        print("RewardVideoAd onVideoComplete");
    },
    onSjmAdClosed: (String id) {
        print("RewardVideoAd onSjmAdClose");
    },
    onSjmAdError: (String id, int code, String message) {
        print("RewardVideoAd onSjmAdError");
    },
);
```
### Interstital Ad

```
SjmAdSdk.loadInterstitalAd(
    "adId",
    onSjmAdLoaded: (String id) {
        print("InterstitalAd onSjmAdLoad");
    },
    onSjmAdShow: (String id) {
        print("InterstitalAd onSjmAdShow");
    },
    onSjmAdClicked: (String id) {
        print("InterstitalAd onSjmAdClick");
    },
    onSjmAdClosed: (String id) {
        print("InterstitalAd onSjmAdClose");
    },
    onSjmAdError: (String id, int code, String message) {
        print("InterstitalAd onSjmAdError");
    },
);
```


### Native Express Ad 

```
NativeExpressAdView(
    adId:  "adId",
    width: 350,
    height: 300,
    onSjmAdLoaded: (String id) {
        print("native_express onSjmAdLoad");
    },
    onSjmAdShow: (String id) {
        print("native_express onSjmAdShow");
    },
    onSjmAdClicked: (String id) {
        print("native_express onSjmAdClick");
    },
    onSjmAdClosed: (String id) {
        print("native_express onSjmAdClose");
    },
    onSjmAdError: (String id, int code, String message) {
        print("native_express onError");
    },
)
```

### Banner Ad

```
BannerAdView(
    adId: "adId",
    width: 400,
    height: 60,
    onSjmAdLoaded: (String id) {
        print("BannerAd onSjmAdLoad");
    },
    onSjmAdShow: (String id) {
        print("BannerAd onSjmAdShow");
    },
    onSjmAdClicked: (String id) {
        print("BannerAd onSjmAdClick");
    },
    onSjmAdClosed: (String id) {
        print("BannerAd onSjmAdClose");
    },
    onSjmAdError: (String id, int code, String message) {
        print("BannerAd onError");
    },
)
```

