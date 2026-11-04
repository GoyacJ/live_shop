import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'config_util.dart';

/// 语言工具类
/// 用于管理应用的语言设置，提供语言切换功能
class LanguageUtil {
  // 私有构造函数
  LanguageUtil._();

  /// 支持的语言列表
  static List<Locale> supportedLocales = [
    const Locale('zh', 'CN'), // 简体中文
    const Locale('en', 'US'), // 英文
  ];

  /// 获取当前语言
  /// 优先级：已保存的设置 > 设备语言 > 默认语言(zh_CN)
  static Future<Locale> getCurrentLanguage() async {
    // 获取已保存的语言设置
    String? languageCode = await ConfigUtil.instance.getLanguageCode();
    String? countryCode = await ConfigUtil.instance.getCountryCode();

    // 如果没有保存的设置，使用设备语言
    if (languageCode == null || countryCode == null) {
      final deviceLocale = Get.deviceLocale;
      languageCode = deviceLocale?.languageCode ?? 'zh';
      countryCode = deviceLocale?.countryCode ?? 'CN';
    }

    return Locale(languageCode, countryCode);
  }

  /// 保存语言设置
  /// [languageCode] 语言代码，例如：'zh'、'en'
  /// [countryCode] 国家代码，例如：'CN'、'US'
  static Future<void> saveLanguage(
    String languageCode, 
    String countryCode,
  ) async {
    await ConfigUtil.instance.setLanguageCode(languageCode);
    await ConfigUtil.instance.setCountryCode(countryCode);
  }

  /// 切换语言
  /// [languageCode] 语言代码，例如：'zh'、'en'
  /// [countryCode] 国家代码，例如：'CN'、'US'
  static Future<void> switchLanguage(
    String languageCode, 
    String countryCode,
  ) async {
    await saveLanguage(languageCode, countryCode);
    Get.updateLocale(Locale(languageCode, countryCode));
  }

  /// 切换到中文
  static Future<void> switchToChinese() async {
    await switchLanguage('zh', 'CN');
  }

  /// 切换到英文
  static Future<void> switchToEnglish() async {
    await switchLanguage('en', 'US');
  }
}
