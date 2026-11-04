import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';
import 'package:flutter/foundation.dart';

class InterstitalPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var adId = 'sjmad_test004';
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJM_I101";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  SjmAdSdk.loadInterstitalAd(
                    adId,
                    onSjmAdLoaded: (String id,int ecpm) {
                      print("InterstitalAd onSjmAdLoad.ecpm: $ecpm");
                    },
                    onSjmAdShow: (String id,int ecpm) {
                      print("InterstitalAd onSjmAdShow");
                    },
                    onSjmAdClicked: (String id) {
                      print("InterstitalAd onSjmAdClick");
                    },
                    onSjmAdClosed: (String id) {
                      print("InterstitalAd onSjmAdClose");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("InterstitalAd onSjmAdError" + message);
                    },
                  );
                },
                child: Text("加载插屏广告")),
          ],
        ),
      ),
    );
  }
}
