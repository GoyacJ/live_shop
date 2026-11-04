import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'sjm_ad.dart';

/// Widget for banner ad
class ContentNovelAdView extends StatelessWidget {
  final String? adId;
  final AdCallback? onSjmAdShow;
  final AdCallback? onSjmAdClicked;
  final AdErrorCallback? onSjmAdError;
  final double? width;
  final double? height;
  ContentNovelAdView(
      {Key? key,
      this.adId,
      this.width,
      this.height,
      this.onSjmAdShow,
      this.onSjmAdClicked,
      this.onSjmAdError})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    Widget content;
    if (defaultTargetPlatform == TargetPlatform.android) {
      content = AndroidView(
        viewType: 'flutter_adsjm_plugin/content_novel',
        creationParams: {"adId": adId},
        creationParamsCodec: const StandardMessageCodec(),
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    } else {
      content = Text("Not supported");
    }

    return Container(
      width: width,
      height: height,
      child: content,
    );
  }

  void _onPlatformViewCreated(int id) {
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/content_novel_event_$id");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"]);
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"]);
          break;
        case "onSjmAdError":
          onSjmAdError?.call(event["id"], event["code"], event["message"]);
          break;
      }
    });
  }
}
