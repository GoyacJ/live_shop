import 'package:flutter/material.dart';
import 'package:adsjmsdk/content_novel_ad_view.dart';

class ContentNovelPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final width = MediaQuery.of(context).size.width; //宽度 411.4
    final height = MediaQuery.of(context).size.height - 56; //高度 683.4
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ContentNovelAdView(
              adId: "sjmad_test019",
              width: width,
              height: height,
              onSjmAdShow: (String id) {
                print("content_novel onSjmAdShow");
              },
              onSjmAdClicked: (String id) {
                print("content_novel onSjmAdClick");
              },
              onSjmAdError: (String id, int code, String message) {
                print("content_novel onError");
              },
            ),
          ],
        ),
      ),
    );
  }
}
