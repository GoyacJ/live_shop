import 'package:flutter/material.dart';
import 'package:adsjmsdk/banner_ad_view.dart';
import 'package:flutter/foundation.dart';

class BannerPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var adId = 'sjmad_test003';
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJM_B003";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            BannerAdView(
              adId: adId,
              width: 400,
              height: 100,
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
            ),
          ],
        ),
      ),
    );
  }
}
