import 'package:flutter/services.dart';

typedef AdTradeCallback = void Function(String id, String key);
typedef AdCallback = void Function(String id);
typedef AdNewCallback = void Function(String id,int ecpm);
typedef AdErrorCallback = void Function(String id, int code, String message);
typedef AdSuccesCallback = void Function(
    int reward, int stepNum, bool isMultipleReward);

/// API for showing various types of advertisements
class SjmAdSdk {
  // static int _channelId = 0;
  static MethodChannel _methodChannel =
      new MethodChannel("flutter_adsjm_plugin/method");

  /// set user id on login and logout
  // static void setUserId(String userId) {
  //   _methodChannel.invokeMethod("setUserId", {"userId": userId});
  // }

  //sdk init
  static void init(String appId) {
    _methodChannel.invokeMethod("init", {"appId": appId});
  }

  /// show reward video ad
  static void showRewardVideoAd(String adId, String userId,
      {AdTradeCallback? onSjmAdTradeId,
        AdNewCallback? onSjmAdLoaded,
        AdNewCallback? onSjmAdShow,
      AdCallback? onSjmAdReward,
      AdCallback? onSjmAdClicked,
      AdCallback? onSjmAdVideoComplete,
      AdCallback? onSjmAdClosed,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod("showRewardVideoAd",
        {"_channelId": "rewardVideo", "adId": adId, "userId": userId});

    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_rewardVideo");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdTradeId":
          onSjmAdTradeId?.call(event["id"] ?? '', event["key"] ?? '');
          break;
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"] ?? '',event['ecpm']);
          break;

        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"] ?? '',event['ecpm']);
          break;

        case "onSjmAdReward":
          onSjmAdReward?.call(event["id"] ?? '');
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"] ?? '');
          break;

        case "onSjmAdVideoComplete":
          onSjmAdVideoComplete?.call(event["id"] ?? '');
          break;

        case "onSjmAdClosed":
          onSjmAdClosed?.call(event["id"] ?? '');
          break;

        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  /// show interstitial ad
  static void loadInterstitalAd(String adId,
      {AdNewCallback? onSjmAdLoaded,
        AdNewCallback? onSjmAdShow,
      AdCallback? onSjmAdClicked,
      AdCallback? onSjmAdClosed,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod(
        "loadInterstitalAd", {"_channelId": "interstital", "adId": adId});
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_interstital");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"] ?? '',event['ecpm']);
          break;

        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"] ?? '',event['ecpm']);
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"] ?? '');
          break;

        case "onSjmAdClosed":
          onSjmAdClosed?.call(event["id"] ?? '');
          break;

        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  /// show volice ad
  static void loadVoliceAd(String adId, String userId, String rewardName,
      {AdCallback? onSjmAdLoad,
      AdCallback? onSjmAdShow,
      AdSuccesCallback? onSjmAdSuccess,
      AdCallback? onSjmAdClose,
      AdCallback? onSjmVoiceComplete,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod("loadVoliceAd",
        {"adId": adId, "userId": userId, "rewardName": rewardName});
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_volice");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdLoad":
          onSjmAdLoad?.call(event["id"] ?? '');
          break;

        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"] ?? '');
          break;

        case "onSjmAdSuccess":
          onSjmAdSuccess?.call(
              event["reward"], event["stepNum"], event["isMultipleReward"]);
          break;

        case "onSjmAdClose":
          onSjmAdClose?.call(event["id"] ?? '');
          break;
        case "onSjmVoiceComplete":
          onSjmVoiceComplete?.call(event["id"] ?? '');
          break;
        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  static void loadNewsAd(String adId, String userId,
      {AdCallback? onSuccess, AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod("loadNewAd", {"adId": adId, "userId": userId});
    EventChannel eventChannel = EventChannel("flutter_adsjm_plugin/event_news");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSuccess":
          onSuccess?.call(event["id"] ?? '');
          break;
        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  /// show full video ad
  static void showFullVideoAd(String adId,
      {AdNewCallback? onSjmAdLoaded,
      AdCallback? onSjmAdVideoCached,
        AdNewCallback? onSjmAdShow,
      AdCallback? onSjmAdClicked,
      AdCallback? onSjmAdVideoComplete,
      AdCallback? onSjmAdClosed,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod(
        "showFullVideoAd", {"_channelId": "fullVideo", "adId": adId});
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_fullVideo");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"] ?? '',event['ecpm']);
          break;
        case "onSjmAdVideoCached":
          onSjmAdVideoCached?.call(event["id"] ?? '');
          break;
        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"] ?? '',event['ecpm']);
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"] ?? '');
          break;

        case "onSjmAdVideoComplete":
          onSjmAdVideoComplete?.call(event["id"] ?? '');
          break;

        case "onSjmAdClosed":
          onSjmAdClosed?.call(event["id"] ?? '');
          break;

        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  static void loadH5contentAd(
      String adId, String userId, String username, String userhead,
      {AdCallback? onIntegralNotEnough,
      AdCallback? onIntegralExpend,
      AdCallback? onFinishTasks,
      AdCallback? onGameExit,
      AdCallback? onSjmAdRewardFinish,
      AdCallback? onSjmAdReward,
      AdCallback? onSjmAdLoaded,
      AdCallback? onSjmAdTradeId,
      AdCallback? onSjmAdClick,
      AdCallback? onSjmUserBehavior,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod("loadH5contentAd", {
      "_channelId": "h5content",
      "adId": adId,
      "userId": userId,
      "username": username,
      "userhead": userhead
    });
    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_h5content");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onIntegralNotEnough":
          onIntegralNotEnough?.call(event["id"] ?? '');
          break;

        case "onIntegralExpend":
          onIntegralExpend?.call(event["id"] ?? '');
          break;
        case "onFinishTasks":
          onFinishTasks?.call(event["id"] ?? '');
          break;
        case "onGameExit":
          onGameExit?.call(event["id"] ?? '');
          break;
        case "onSjmAdRewardFinish":
          onSjmAdRewardFinish?.call(event["id"] ?? '');
          break;
        case "onSjmAdClick":
          onSjmAdClick?.call(event["id"] ?? '');
          break;

        case "onSjmAdReward":
          onSjmAdReward?.call(event["id"] ?? '');
          break;
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"] ?? '');
          break;
        case "onSjmAdTradeId":
          onSjmAdTradeId?.call(event["id"] ?? '');
          break;
        case "onSjmUserBehavior":
          onSjmUserBehavior?.call(event["id"] ?? '');
          break;
        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }

  static void loadVideocontentAd(String adId) {
    _methodChannel.invokeMethod(
        "loadVideocontentAd", {"_channelId": "videocontent", "adId": adId});
  }

  /// show Splash ad
  static void showSplashAd(String adId, int fetchDelay,
      {AdCallback? onSjmAdLoaded,
      AdCallback? onSjmAdShow,
      AdCallback? onSjmAdClicked,
      AdCallback? onSjmAdClosed,
      AdErrorCallback? onSjmAdError}) {
    _methodChannel.invokeMethod("showSplashAd",
        {"_channelId": "splash", "adId": adId, "fetchDelay": fetchDelay});

    EventChannel eventChannel =
        EventChannel("flutter_adsjm_plugin/event_splash");
    eventChannel.receiveBroadcastStream().listen((event) {
      switch (event["event"]) {
        case "onSjmAdLoaded":
          onSjmAdLoaded?.call(event["id"] ?? '');
          break;

        case "onSjmAdShow":
          onSjmAdShow?.call(event["id"] ?? '');
          break;

        case "onSjmAdClicked":
          onSjmAdClicked?.call(event["id"] ?? '');
          break;

        case "onSjmAdClosed":
          onSjmAdClosed?.call(event["id"] ?? '');
          break;

        case "onSjmAdError":
          onSjmAdError?.call(
              event["id"] ?? '', event["code"], event["message"]);
          break;
      }
    });
  }
}
