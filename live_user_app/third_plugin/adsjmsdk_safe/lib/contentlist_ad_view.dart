import 'package:adsjmsdk/sjm_ad.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

/// Widget for banner ad
class ContentListAdView extends StatelessWidget {
  final String? adId;
  final double? width;
  final double? height;
  final AdCallback? onPageEnter;
  final AdCallback? onPageResume;
  final AdCallback? onPagePause;
  final AdCallback? onPageLeave;
  final AdCallback? onVideoPlayStart;
  final AdCallback? onVideoPlayPaused;
  final AdCallback? onVideoPlayResume;
  final AdCallback? onVideoPlayCompleted;
  final AdCallback? onVideoPlayError;
  final AdErrorCallback? onSjmAdError;

  ContentListAdView(
      {Key? key,
      this.adId,
      this.width,
      this.height,
      this.onPageEnter,
      this.onPageResume,
      this.onPagePause,
      this.onPageLeave,
      this.onVideoPlayStart,
      this.onVideoPlayPaused,
      this.onVideoPlayResume,
      this.onVideoPlayCompleted,
      this.onVideoPlayError,
      this.onSjmAdError})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    Widget content;
    if (defaultTargetPlatform == TargetPlatform.android) {
      content = AndroidView(
        viewType: 'flutter_adsjm_plugin/contentlist',
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
        EventChannel("flutter_adsjm_plugin/contentlist_event_$id");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onPageEnter":
          onPageEnter?.call(event["id"]);
          break;

        case "onPageResume":
          onPageResume?.call(event["id"]);
          break;

        case "onPagePause":
          onPagePause?.call(event["id"]);
          break;

        case "onPageLeave":
          onPageLeave?.call(event["id"]);
          break;

        case "onVideoPlayStart":
          onVideoPlayStart?.call(event["id"]);
          break;

        case "onVideoPlayPaused":
          onVideoPlayPaused?.call(event["id"]);
          break;

        case "onVideoPlayResume":
          onVideoPlayResume?.call(event["id"]);
          break;

        case "onVideoPlayCompleted":
          onVideoPlayCompleted?.call(event["id"]);
          break;

        case "onVideoPlayError":
          onVideoPlayError?.call(event["id"]);
          break;

        case "onSjmAdError":
          onSjmAdError?.call(event["id"], event["code"], event["message"]);
          break;
      }
    });
  }
}
