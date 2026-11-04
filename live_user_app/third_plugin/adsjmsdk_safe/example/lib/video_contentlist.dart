import 'package:adsjmsdk/contentlist_ad_view.dart';
import 'package:flutter/material.dart';

class ContentListPage extends StatelessWidget {
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
            ContentListAdView(
              adId: "sjmad_test012",
              width: width,
              height: height,
              onPageEnter: (String id) {
                print("ContentListAdView onPageEnter");
              },
              onPageResume: (String id) {
                print("ContentListAdView onPageResume");
              },
              onPagePause: (String id) {
                print("ContentListAdView onPagePause");
              },
              onPageLeave: (String id) {
                print("ContentListAdView onPageLeave");
              },
              onVideoPlayStart: (String id) {
                print("ContentListAdView onVideoPlayStart");
              },
              onVideoPlayPaused: (String id) {
                print("ContentListAdView onVideoPlayPaused");
              },
              onVideoPlayResume: (String id) {
                print("ContentListAdView onVideoPlayResume");
              },
              onVideoPlayCompleted: (String id) {
                print("ContentListAdView onVideoPlayCompleted");
              },
              onVideoPlayError: (String id) {
                print("ContentListAdView onVideoPlayError");
              },
              onSjmAdError: (String id, int code, String message) {
                print("ContentListAdView onError");
              },
            ),
          ],
        ),
      ),
    );
  }
}
