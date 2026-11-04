import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:live_app/init/config.dart';
import '../../utils/language_util.dart';
import '../services/error_handler.dart';
import '../services/theme_service.dart';
import '../services/user_info_ctl.dart';
import '../utils/config_util.dart';
import '../utils/color_util.dart';
import '../utils/sp_util.dart';

/// 应用初始化类
class AppInit {
  /// 初始化
  static Future<void> init() async {
    // 确保Flutter Widgets初始化
    WidgetsFlutterBinding.ensureInitialized();
    // 初始化SpUtil
    await ConfigUtil.instance.init();

    // 初始化语言设置
    final locale = await LanguageUtil.getCurrentLanguage();
    Get.locale = locale;

    // 设置屏幕方向
    // await SystemChrome.setPreferredOrientations([
    //   DeviceOrientation.portraitUp,
    //   DeviceOrientation.portraitDown,
    // ]);
    await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    // 设置状态栏样式
    SystemChrome.setSystemUIOverlayStyle(
      const SystemUiOverlayStyle(
        statusBarColor: ColorUtil.transparent,
        statusBarIconBrightness: Brightness.dark,
        statusBarBrightness: Brightness.light,
      ),
    );
    // 初始化其他服务
    await _initServices();
  }

  /// 初始化GetX服务
  static Future<void> _initServices() async {
    // 初始化错误处理
    await Get.putAsync(() async => ErrorHandler()..init());

    // 初始化主题服务
    await Get.putAsync(() async {
      final service = ThemeService();
      await service.init();
      return service;
    });

    // 检查是否同意隐私政策
    final isAgreement =
        SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
    if (isAgreement) {
      // 只有在用户同意隐私政策后才初始化用户信息服务
      Get.put<UserInfoCtl>(UserInfoCtl());
    }
  }

  /// 释放资源
  static Future<void> dispose() async {
    // 这里可以添加需要释放的资源
  }

}
