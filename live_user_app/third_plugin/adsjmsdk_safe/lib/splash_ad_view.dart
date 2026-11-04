import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'sjm_ad.dart';

/// Widget for splash ad
class SplashAdView extends StatelessWidget {
  final String? adId;
  final double? width;
  final double? height;
  final AdCallback? onSjmAdLoaded;
  final AdCallback? onSjmAdShow;
  final AdCallback? onSjmAdClicked;
  final AdCallback? onSjmAdClosed;
  final AdErrorCallback? onSjmAdError;

  SplashAdView(
      {Key? key,
      this.adId,
      this.width,
      this.height,
      this.onSjmAdLoaded,
      this.onSjmAdShow,
      this.onSjmAdClicked,
      this.onSjmAdClosed,
      this.onSjmAdError})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    Widget splash;
    if (defaultTargetPlatform == TargetPlatform.android) {
      splash = AndroidView(
        viewType: 'flutter_adsjm_plugin/splash',
        creationParams: {
          "adId": adId,
          "width": width,
          "height": height,
        },
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      splash = UiKitView(
        viewType: 'flutter_adsjm_plugin/splash',
        creationParams: {
          "adId": adId,
          "width": width,
          "height": height,
        },
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else {
      splash = Text("Not supported");
    }

    return Container(
      width: width,
      height: height,
      child: splash,
    );
  }

  void _onPlatformViewCreated(int id) {
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/splash_event_$id");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"]);
          break;

        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"]);
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"]);
          break;

        case "onSjmAdClosed":
          onSjmAdClosed?.call(event["id"]);
          break;

        case "onSjmAdError":
          onSjmAdError?.call(event["id"], event["code"], event["message"]);
          break;
      }
    });
  }
}
