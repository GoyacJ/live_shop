import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';
import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:fluwx/fluwx.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/utils/toast_util.dart';

import '../init/config.dart';
import '../widgets/easy_loading_mixin.dart';
import 'http_util.dart';

/// Created by Jcy
/// on 2025/5/21
///
class WechatUtil with EasyLoadingMixin {
  static WechatUtil? _instance;

  /// 返回WechatUtil单例
  static WechatUtil get instance => _getInstance();

  /// 使用双重检查锁定模式实现单例
  static WechatUtil _getInstance() {
    _instance ??= WechatUtil.init();
    return _instance!;
  }

  final Fluwx _fluwx = Fluwx();

  // 三个独立的监听器函数
  Function(WeChatResponse)? _paymentListener;
  Function(WeChatResponse)? _shareListener;
  Function(WeChatResponse)? _authListener;

  /// 初始化微信API
  WechatUtil.init() {
    _fluwx.registerApi(appId: Config.apiHost.wxAppId, universalLink: '');
  }

  /// 移除支付监听
  void _removePaymentListener() {
    if (_paymentListener != null) {
      _fluwx.removeSubscriber(_paymentListener!);
      _paymentListener = null;
    }
  }

  /// 移除分享监听
  void _removeShareListener() {
    if (_shareListener != null) {
      _fluwx.removeSubscriber(_shareListener!);
      _shareListener = null;
    }
  }

  /// 移除登录监听
  void _removeAuthListener() {
    if (_authListener != null) {
      _fluwx.removeSubscriber(_authListener!);
      _authListener = null;
    }
  }

  /// 微信支付
  Future<void> toWxPay(
    Map result, {
    Function(int code, String? errStr)? onPayResult,
  }) async {
    // 先清除之前的支付监听
    _removePaymentListener();

    final isInstalled = await _fluwx.isWeChatInstalled;
    if (!isInstalled) {
      ToastUtil.showToast("请先安装微信");
      onPayResult?.call(-1, "请先安装微信");
      return;
    }

    try {
      showEasyLoading();
      await _fluwx.pay(
          which: Payment(
        appId: result['appid'].toString(),
        partnerId: result['partnerid'].toString(),
        prepayId: result['prepayid'].toString(),
        packageValue: result['package'].toString(),
        nonceStr: result['noncestr'].toString(),
        timestamp: NumUtil.getIntByValueStr(result['timestamp'].toString()) ?? 0,
        sign: result['sign'].toString(),
      ));
      _listenPaymentResult(onPayResult: onPayResult);
    } catch (e) {
      ToastUtil.showToast("微信支付异常");
      LogUtils.e(e.toString());
      onPayResult?.call(-1, "微信支付异常：${e.toString()}");
    } finally {
      dismissEasyLoading();
    }
  }

  /// 监听支付结果
  void _listenPaymentResult({Function(int code, String? errStr)? onPayResult}) {
    void payListener(WeChatResponse data) {
      if (data is WeChatPaymentResponse) {
        if (data.errCode == 0) {
          ToastUtil.showToast("微信支付成功");
          onPayResult?.call(0, null);
        } else {
          ToastUtil.showToast("微信支付失败：${data.errStr ?? ''}");
          onPayResult?.call(data.errCode ?? -1, data.errStr);
        }
        // 支付完成后清除监听
        _removePaymentListener();
      }
    }

    _paymentListener = payListener;
    _fluwx.addSubscriber(_paymentListener!);
  }

  /// 微信分享图片
  Future<void> toWxShareImage(
    String url, {
    String? title,
    String? description,
    String? imageUrl,
    WeChatScene scene = WeChatScene.session,
    Function(int code, String? errStr)? onShareResult,
  }) async {
    // 先清除之前的分享监听
    _removeShareListener();

    if (!await _fluwx.isWeChatInstalled) {
      ToastUtil.showToast("请先安装微信");
      onShareResult?.call(-1, "请先安装微信");
      return;
    }

    final model = WeChatShareImageModel(
      WeChatImageToShare(
        uint8List: imageUrl == null ? null : await fetchImageAsUint8List(imageUrl),
      ),
      title: title,
      description: description,
      scene: scene,
    );

    try {
      await _fluwx.share(model);
      _listenShareResult(onShareResult: onShareResult);
    } catch (e) {
      ToastUtil.showToast("分享图片异常");
      LogUtils.e(e.toString());
      onShareResult?.call(-1, "分享图片异常：${e.toString()}");
    }
  }

  /// 微信分享网页
  Future<void> toWxShareWeb(
    String url, {
    required String title,
    required String description,
    required String imageUrl,
    WeChatScene scene = WeChatScene.session,
    Function(int code, String? errStr)? onShareResult,
  }) async {
    // 先清除之前的分享监听
    _removeShareListener();

    if (!await _fluwx.isWeChatInstalled) {
      ToastUtil.showToast("请先安装微信");
      onShareResult?.call(-1, "请先安装微信");
      return;
    }
    final model = WeChatShareWebPageModel(
      url,
      title: title,
      description: description,
      scene: scene,
    );

    try {
      await _fluwx.share(model);
      _listenShareResult(onShareResult: onShareResult);
    } catch (e) {
      ToastUtil.showToast("分享网页异常");
      LogUtils.e(e.toString());
      onShareResult?.call(-1, "分享网页异常：${e.toString()}");
    }
  }

  /// 监听分享结果
  void _listenShareResult({Function(int code, String? errStr)? onShareResult}) {
    void shareListener(WeChatResponse data) {
      if (data is WeChatShareResponse) {
        if (data.errCode == 0) {
          ToastUtil.showToast("分享成功");
          onShareResult?.call(0, null);
        } else {
          ToastUtil.showToast("分享失败：${data.errStr ?? ''}");
          onShareResult?.call(data.errCode ?? -1, data.errStr);
        }
        // 分享完成后清除监听
        _removeShareListener();
      }
    }

    _shareListener = shareListener;
    _fluwx.addSubscriber(_shareListener!);
  }

  /// 微信登录
  Future<void> toWxLogin({
    Function(int code, String? errStr)? onResult,
  }) async {
    // 先清除之前的登录监听
    _removeAuthListener();

    if (!await _fluwx.isWeChatInstalled) {
      ToastUtil.showToast("请先安装微信");
      onResult?.call(-1, "请先安装微信");
      return;
    }
    try {
      await _fluwx.authBy(which: NormalAuth(scope: 'snsapi_userinfo', state: 'wx_login_state'));
      _listenLoginResult(onResult: onResult);
    } catch (e) {
      ToastUtil.showToast("微信登录异常");
      LogUtils.e(e.toString());
      onResult?.call(-1, "微信登录异常：${e.toString()}");
    }
  }

  /// 监听微信登录结果
  void _listenLoginResult({
    Function(int code, String? errStr)? onResult,
  }) {
    void authListener(WeChatResponse response) {
      if (response is WeChatAuthResponse) {
        switch (response.errCode) {
          case 0:
            ToastUtil.showToast("登录成功");
            onResult?.call(0, response.code);
            break;
          case -2:
            ToastUtil.showToast("用户取消授权");
            onResult?.call(-2, "用户取消授权");
            break;
          case -4:
            ToastUtil.showToast("用户拒绝授权");
            onResult?.call(-4, "用户拒绝授权");
            break;
          default:
            ToastUtil.showToast("微信登录失败");
            onResult?.call(response.errCode ?? -1, "未知错误");
        }
        // 登录完成后清除监听
        _removeAuthListener();
      }
    }

    _authListener = authListener;
    _fluwx.addSubscriber(_authListener!);
  }

  /// 获取 accessToken 和 openId
  Future<Map<String, String>?> getWeChatAccessToken(String code) async {
    final path =
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${Config.apiHost.wxAppId}&secret=${Config.apiHost.wxSecret}&code=$code&grant_type=authorization_code";

    try {
      final response = await HttpUtil.instance.get(path);
      if (response.statusCode == 200) {
        final mData = json.decode(response.toString());
        return {
          "access_token": mData["access_token"].toString(),
          "openid": mData["openid"].toString(),
        };
      } else {
        LogUtils.e("Token获取失败：${response.statusMessage}");
      }
    } catch (e) {
      LogUtils.e("Token异常：$e");
    }
    return null;
  }

  /// 拉取微信用户信息
  Future<Map<String, dynamic>?> getWxUserInfo({
    required String accessToken,
    required String openId,
  }) async {
    final path = "https://api.weixin.qq.com/sns/userinfo?access_token=$accessToken&openid=$openId";

    try {
      final response = await HttpUtil.instance.get(path);
      if (response.statusCode == 200) {
        final data = json.decode(response.toString());
        print("微信用户信息：$data");
        return data;
      }
    } catch (e) {
      LogUtils.e("用户信息拉取异常：$e");
    }
    return null;
  }

  Future<Uint8List?> fetchImageAsUint8List(String imageUrl) async {
    try {
      final response = await HttpUtil.instance.get(imageUrl);
      if (response.statusCode == 200) {
        return response.data.bodyBytes;
      } else {
        debugPrint('Failed to load image: ${response.statusCode}');
        return null;
      }
    } catch (e) {

      debugPrint('Error fetching image: $e');
      return null;
    }
  }
}
