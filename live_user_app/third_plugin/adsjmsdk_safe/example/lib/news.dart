import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';

class NewsPage extends StatelessWidget {
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
                  SjmAdSdk.loadNewsAd(
                    "sjmad_test018",
                    "userid",
                    onSuccess: (String id) {
                      print("NewsAd onSuccess");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("NewsAd onSjmAdError");
                    },
                  );
                },
                child: Text("加载新闻咨询")),
          ],
        ),
      ),
    );
  }
}
