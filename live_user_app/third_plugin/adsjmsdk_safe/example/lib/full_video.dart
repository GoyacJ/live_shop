import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';
import 'package:flutter/foundation.dart';

class FullVideoPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var adId = 'sjmad_test006';
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJMKS_FV001";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  SjmAdSdk.showFullVideoAd(
                    adId,
                    onSjmAdLoaded: (String id,int ecpm) {
                      print("FullVideoAd onSjmAdLoad111");
                    },
                    onSjmAdShow: (String id,int ecpm) {
                      print("FullVideoAd onSjmAdShow111");
                    },
                    onSjmAdClicked: (String id) {
                      print("FullVideoAd onSjmAdClick");
                    },
                    onSjmAdVideoComplete: (String id) {
                      print("FullVideoAd onVideoComplete");
                    },
                    onSjmAdClosed: (String id) {
                      print("FullVideoAd onSjmAdClose111");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("FullVideoAd onSjmAdError" + message);
                    },
                  );
                },
                child: Text("加载全屏视频广告")),
          ],
        ),
      ),
    );
  }
}
