import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../utils/color_util.dart';
import '../utils/sp_util.dart';

/// 主题服务
/// 管理应用主题切换和持久化
class ThemeService extends GetxService {
  static ThemeService get to => Get.find();

  final _isDarkMode = false.obs;
  final _themeKey = 'theme_mode';

  /// 是否是暗黑模式
  bool get isDarkMode => _isDarkMode.value;

  /// 初始化主题
  Future<void> init() async {
    // 从本地存储读取主题设置
    final savedTheme = SpUtil.getData<bool>(_themeKey) ?? false;
    _isDarkMode.value = savedTheme;

    // 应用主题
    _applyTheme();
  }

  /// 切换主题
  void toggleTheme() {
    _isDarkMode.value = !_isDarkMode.value;
    // 保存主题设置
    SpUtil.setData(_themeKey, _isDarkMode.value);
    // 应用主题
    _applyTheme();
  }

  /// 应用主题
  void _applyTheme() {
    Get.changeThemeMode(_isDarkMode.value ? ThemeMode.dark : ThemeMode.light);
  }

  /// 获取主题配置
  ThemeData get lightTheme => ThemeData(
        useMaterial3: false,
        cupertinoOverrideTheme:
            const CupertinoThemeData(brightness: Brightness.light),
        tabBarTheme: TabBarThemeData(
          overlayColor: WidgetStateProperty.all(ColorUtil.transparent), // 去掉点击效果
        ),
        primarySwatch: ColorUtil.blue,
        brightness: Brightness.light,
        scaffoldBackgroundColor: ColorUtil.grey[100],
        appBarTheme: const AppBarTheme(
          elevation: 0,
          centerTitle: true,
          backgroundColor: ColorUtil.white,
          iconTheme: IconThemeData(color: ColorUtil.black),
          titleTextStyle: TextStyle(
            color: ColorUtil.black,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      );

  ThemeData get darkTheme => ThemeData(
        useMaterial3: false,
        cupertinoOverrideTheme:
            const CupertinoThemeData(brightness: Brightness.dark),
        tabBarTheme: TabBarThemeData(
          overlayColor: WidgetStateProperty.all(ColorUtil.transparent), // 去掉点击效果
        ),
        primarySwatch: ColorUtil.blue,
        brightness: Brightness.dark,
        scaffoldBackgroundColor: ColorUtil.grey[900],
        appBarTheme: AppBarTheme(
          elevation: 0,
          centerTitle: true,
          backgroundColor: ColorUtil.grey[850],
          iconTheme: const IconThemeData(color: ColorUtil.white),
          titleTextStyle: const TextStyle(
            color: ColorUtil.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      );
}
