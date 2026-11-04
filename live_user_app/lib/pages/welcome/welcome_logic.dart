import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:flutter/cupertino.dart';

import '../../routes/app_pages.dart';

class WelcomeLogic extends BaseController {
  // 防止重复跳转的标记
  bool _hasNavigated = false;
  bool showAd = false;
  Timer? _timer;

  @override
  initData() {
    _timer = Timer.periodic(const Duration(seconds: 3), (timer) {
      goToMain();
    });
  }

  /// 跳转到主页面
  /// 包含防重复跳转逻辑
  goToMain() {
    // 防止重复跳转
    if (_hasNavigated) return;
    // 检查当前路由，确保还在欢迎页
    if (Get.currentRoute != '/') return;
    _hasNavigated = true;
    // 延迟执行跳转，确保当前帧渲染完成
    Future.delayed(const Duration(milliseconds: 100), () {
      if (!_hasNavigated) return; // 二次检查
      Get.offAllNamed(AppPages.main);
    });
  }

  @override
  void onClose() {
    dismissEasyLoading();
    _hasNavigated = true; // 页面销毁时标记已跳转
    _timer?.cancel();
    super.onClose();
  }
}
