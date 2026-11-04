import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';

class VolicePage extends StatelessWidget {
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
                  SjmAdSdk.loadVoliceAd(
                    "sjmad_test017",
                    "userid",
                    "rewardname",
                    onSjmAdLoad: (String id) {
                      print("VoliceAd onSjmAdLoad");
                    },
                    onSjmAdShow: (String id) {
                      print("VoliceAd onSjmAdShow");
                    },
                    onSjmAdSuccess:
                        (int reward, int stepNum, bool isMultipleReward) {
                      print("VoliceAd onSjmAdSuccess.isMultipleReward=" +
                          isMultipleReward.toString() +
                          ",,stepNum=" +
                          stepNum.toString());
                    },
                    onSjmAdClose: (String id) {
                      print("VoliceAd onSjmAdClose");
                    },
                    onSjmVoiceComplete: (String id) {
                      print("VoliceAd onSjmVoiceComplete");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("VoliceAd onSjmAdError");
                    },
                  );
                },
                child: Text("加载语音广告")),
          ],
        ),
      ),
    );
  }
}
