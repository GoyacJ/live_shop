import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';

class H5ContentPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  SjmAdSdk.loadH5contentAd(
                    "sjmad_test00h5",
                    "userid",
                    "username",
                    "userhead",
                    onIntegralNotEnough: (String id) {
                      print("H5ContentPage onIntegralNotEnough");
                    },
                    onFinishTasks: (String id) {
                      print("H5ContentPage onFinishTasks");
                    },
                    onGameExit: (String id) {
                      print("H5ContentPage onGameExit");
                    },
                    onIntegralExpend: (String id) {
                      print("H5ContentPage onIntegralExpend");
                    },
                    onSjmAdReward: (String id) {
                      print("H5ContentPage onSjmAdReward");
                    },
                    onSjmAdRewardFinish: (String id) {
                      print("H5ContentPage onSjmAdRewardFinish");
                    },
                    onSjmAdTradeId: (String id) {
                      print("H5ContentPage onSjmAdTradeId");
                    },
                    onSjmUserBehavior: (String id) {
                      print("H5ContentPage onSjmUserBehavior");
                    },
                    onSjmAdClick: (String id) {
                      print("H5ContentPage onSjmAdClick");
                    },
                    onSjmAdLoaded: (String id) {
                      print("H5ContentPage onSjmAdLoaded");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("H5ContentPage onSjmAdError");
                    },
                  );
                },
                child: Text("加载H5广告")),
          ],
        ),
      ),
    );
  }
}
