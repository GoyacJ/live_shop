import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'sjm_ad.dart';

/// Widget for banner ad
class NativeExpressAdView extends StatelessWidget {
  final String? adId;
  final double? width;
  final double? height;
  final AdCallback? onSjmAdLoaded;
  final AdCallback? onSjmAdShow;
  final AdCallback? onSjmAdClicked;
  final AdCallback? onSjmAdClosed;
  final AdErrorCallback? onSjmAdError;

  NativeExpressAdView(
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

  static MethodChannel _methodChannel =
      new MethodChannel("flutter_adsjm_plugin/webAd_method");

  /**iOS 使用 - 遇到点击穿透的情况使用该方法
   * isCovered 是否覆盖
   * x,y,w,h 覆盖范围 （x,y 从广告展示位置开始)
   * */
  static void updateCoverBounds(
    bool isCovered,
    double x,
    double y,
    double w,
    double h,
  ) {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      _methodChannel.invokeMethod("updateCoverBounds",
          {'isCovered': isCovered, 'x': x, 'y': y, 'w': w, 'h': h});
    }
  }

  //iOS 使用 - 点击穿透调试
  static void enableDebug(bool enable) {
    if (kDebugMode && defaultTargetPlatform == TargetPlatform.iOS) {
      _methodChannel.invokeMethod("enableDebug", {
        'enable': enable,
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    Widget banner;
    if (defaultTargetPlatform == TargetPlatform.android) {
      banner = AndroidView(
        viewType: 'flutter_adsjm_plugin/native_express',
        creationParams: {
          "adId": adId,
          "width": width,
          "height": height,
        },
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      banner = UiKitView(
        viewType: 'flutter_adsjm_plugin/native_express',
        creationParams: {
          "adId": adId,
          "width": width,
          "height": height,
        },
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else {
      banner = Text("Not supported");
    }

    return Container(
      width: width,
      height: height,
      child: banner,
    );
  }

  void _onPlatformViewCreated(int id) {
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/native_express_event_$id");
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
