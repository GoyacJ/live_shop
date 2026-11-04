import 'package:flutter/foundation.dart';

import 'H5Content.dart';
import 'banner.dart';
import 'content_novel.dart';
import 'interstital.dart';
import 'native_express.dart';
import 'splash.dart';
import 'tab.dart';
import 'package:flutter/material.dart';
import 'package:adsjmsdk/sjm_ad.dart';
import 'full_video.dart';
import 'news.dart';
import 'reward_video.dart';
import 'volice.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.grey,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        buttonTheme: ButtonThemeData(minWidth: 200),
      ),
      home: MyHomePage(),
      routes: <String, WidgetBuilder>{
        '/homepage': (BuildContext context) => MyHomePage(),
        '/splash': (BuildContext context) => SplashPage(),
        '/reward-video': (BuildContext context) => RewardVideoPage(),
        '/full-video': (BuildContext context) => FullVideoPage(),
        '/banner': (BuildContext context) => BannerPage(),
        '/native_express': (BuildContext context) => NativeExpressPage(),
        '/interstitial': (BuildContext context) => InterstitalPage(),
        '/h5Content': (BuildContext context) => H5ContentPage(),
        '/tab': (BuildContext context) => Tabs(),
        '/volice': (BuildContext context) => VolicePage(),
        '/news': (BuildContext context) => NewsPage(),
        '/novel': (BuildContext context) => ContentNovelPage(),
      },
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/splash');
                },
                child: Text("开屏 广告-1")),
            ElevatedButton(
                onPressed: () {
                  var adId = 'sjmad_test009'; //sjmad_test009 sjmad_test002
                  if (defaultTargetPlatform == TargetPlatform.iOS) {
                    adId = "SJM_S002";
                  }
                  SjmAdSdk.showSplashAd(
                    adId,
                    5,
                    onSjmAdLoaded: (String id) {
                      print("SplashAd 加载成功");
                    },
                    onSjmAdShow: (String id) {
                      print("SplashAd 展示");
                    },
                    onSjmAdClicked: (String id) {
                      print("SplashAd 点击");
                    },
                    onSjmAdClosed: (String id) {
                      print("SplashAd 关闭");
                    },
                    onSjmAdError: (String id, int code, String message) {
                      print("SplashAd 错误 " + message);
                    },
                  );
                },
                child: Text("[ios]开屏 广告-2")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/reward-video');
                },
                child: Text("激励视频广告")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/full-video');
                },
                child: Text("全屏视频广告")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/banner');
                },
                child: Text("Banner 广告")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/interstitial');
                },
                child: Text("插屏广告")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/native_express');
                },
                child: Text("信息流广告")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/h5Content');
                },
                child: Text("[Android]H5Content")),
            ElevatedButton(
                onPressed: () {
                  SjmAdSdk.loadVideocontentAd("sjmad_test012");
                },
                child: Text("[Android]视频内容跳转")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/tab');
                },
                child: Text("[Android]视频内容tab嵌入")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/volice');
                },
                child: Text("[Android]语音红包")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/news');
                },
                child: Text("[Android]新闻咨询")),
            ElevatedButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/novel');
                },
                child: Text("[Android]小说")),
          ],
        ),
      ),
    );
  }
}
