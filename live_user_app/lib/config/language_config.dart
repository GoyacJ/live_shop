import 'package:flutter/material.dart';

/// 语言配置类
/// 定义支持的语言和相关配置
class LanguageConfig {
  /// 支持的语言列表
  static const List<Locale> supportedLocales = [
    Locale('zh', 'CN'), // 简体中文
    Locale('en', 'US'), // 英文
  ];

  /// 获取语言显示名称
  static String getLanguageName(String locale) {
    switch (locale) {
      case 'zh_CN':
        return '简体中文';
      case 'en_US':
        return 'English';
      default:
        return '简体中文';
    }
  }

  /// 获取语言图标
  static String getLanguageIcon(String locale) {
    switch (locale) {
      case 'zh_CN':
        return '🇨🇳';
      case 'en_US':
        return '🇺🇸';
      default:
        return '🇨🇳';
    }
  }

  /// 将语言代码转换为Locale对象
  static Locale getLocaleFromString(String locale) {
    final parts = locale.split('_');
    return Locale(parts[0], parts[1]);
  }

  /// 将Locale对象转换为语言代码字符串
  static String getStringFromLocale(Locale locale) {
    return '${locale.languageCode}_${locale.countryCode}';
  }
}
