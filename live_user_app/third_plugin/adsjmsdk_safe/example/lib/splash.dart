import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:adsjmsdk/splash_ad_view.dart';
import 'dart:ui';

class SplashPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // final size =(context).size;
    // final width =size.width;
    // final height =size.height;
    // window.physicalSize  //Size(1080.0, 1794.0)  单位px
    MediaQuery.of(context).size; //Size(411.4, 683.4)   单位:dp
    final width = MediaQuery.of(context).size.width; //宽度 411.4
    final height = MediaQuery.of(context).size.height; //高度 683.4

    var adId = 'sjmad_test002'; //sjmad_test009 sjmad_test002
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJM_S001";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            SplashAdView(
              adId: adId,
              width: width,
              height: height,
              onSjmAdLoaded: (String id) {
                print("SplashAd onSjmAdLoad");
              },
              onSjmAdShow: (String id) {
                print("SplashAd onSjmAdShow");
              },
              onSjmAdClicked: (String id) {
                print("SplashAd onSjmAdClick");
              },
              onSjmAdClosed: (String id) {
                print("SplashAd onSjmAdClose");
              },
              onSjmAdError: (String id, int code, String message) {
                print("SplashAd onError " + message);
              },
            ),
          ],
        ),
      ),
    );
  }
}
