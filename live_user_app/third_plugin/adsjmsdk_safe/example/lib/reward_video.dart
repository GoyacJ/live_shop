import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';
import 'package:flutter/foundation.dart';

class RewardVideoPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var adId = 'sjmad_test001';
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJM_R003";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  SjmAdSdk.showRewardVideoAd(
                    adId,
                    "userId",
                    onSjmAdTradeId: (String id, String key) {
                      print("RewardVideoAd onSjmAdTradeId");
                    },
                    onSjmAdLoaded: (String id,int ecpm) {
                      print("RewardVideoAd onSjmAdLoad.ecpm: $ecpm");
                    },
                    onSjmAdShow: (String id,int ecpm) {
                      print("RewardVideoAd onSjmAdShow.ecpm: $ecpm");
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
                },
                child: Text("加载激励视频广告")),
          ],
        ),
      ),
    );
  }
}
