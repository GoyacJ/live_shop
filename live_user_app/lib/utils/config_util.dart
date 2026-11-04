
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/sp_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

/// 配置工具类
/// 用于管理应用全局配置，包括：
/// 1. 主题配置（深色/浅色模式）
/// 2. 语言配置（国际化）
/// 3. 字体大小配置
/// 4. 其他全局设置
class ConfigUtil {
  static final ConfigUtil _instance = ConfigUtil._();
  factory ConfigUtil() => _instance;
  ConfigUtil._();

  static ConfigUtil get instance => _instance;

  static const appName = "颐心相伴";
  static const slogan = "颐心相伴·相伴一生";

  /// 配置存储的key常量
  static const String _themeKey = 'app_theme_mode';
  static const String _languageCodeKey = 'app_language_code';
  static const String _countryCodeKey = 'app_country_code';
  static const String _fontSizeKey = 'app_font_size';
  static const String token = 'token';
  static const String isFirst = 'isFirst';
  static const String isAgreement = 'isAgreement';

  /// 默认配置值
  static const String defaultLanguageCode = 'zh';
  static const String defaultCountryCode = 'CN';
  static const ThemeMode defaultThemeMode = ThemeMode.system;
  static const double defaultFontSize = 14.0;

  /// 初始化配置
  /// 在应用启动时调用，加载所有存储的配置
  Future<void> init() async {
    await SpUtil.init();
    _loadThemeMode();
    _loadLanguage();
    _loadFontSize();
  }

  /// 主题模式相关
  ThemeMode _themeMode = defaultThemeMode;
  
  /// 获取当前主题模式
  ThemeMode get themeMode => _themeMode;

  /// 设置主题模式
  /// [mode] 要设置的主题模式
  /// 支持：system(跟随系统)、light(浅色)、dark(深色)
  Future<void> setThemeMode(ThemeMode mode) async {
    _themeMode = mode;
    await SpUtil.setData(_themeKey, mode.index);
  }

  /// 从存储加载主题模式
  void _loadThemeMode() {
    final index = SpUtil.getData<int>(_themeKey);
    if (index != null) {
      _themeMode = ThemeMode.values[index];
    }
  }

  /// 语言配置相关
  String? _languageCode;
  String? _countryCode;

  /// 获取当前语言代码
  /// 如果未设置，返回null
  Future<String?> getLanguageCode() async {
    if (_languageCode != null) return _languageCode;
    return SpUtil.getData<String>(_languageCodeKey);
  }

  /// 获取当前国家代码
  /// 如果未设置，返回null
  Future<String?> getCountryCode() async {
    if (_countryCode != null) return _countryCode;
    return SpUtil.getData<String>(_countryCodeKey);
  }

  /// 获取完整的语言标识
  String get locale {
    final langCode = _languageCode ?? defaultLanguageCode;
    final ctryCode = _countryCode ?? defaultCountryCode;
    return '${langCode}_$ctryCode';
  }

  /// 设置语言代码
  /// [languageCode] 语言代码，例如：'zh'、'en'
  Future<void> setLanguageCode(String languageCode) async {
    _languageCode = languageCode;
    await SpUtil.setData(_languageCodeKey, languageCode);
  }

  /// 设置国家代码
  /// [countryCode] 国家代码，例如：'CN'、'US'
  Future<void> setCountryCode(String countryCode) async {
    _countryCode = countryCode;
    await SpUtil.setData(_countryCodeKey, countryCode);
  }

  /// 设置完整的语言配置
  /// [locale] 语言配置，格式：'zh_CN'、'en_US'
  Future<void> setLocale(String locale) async {
    final parts = locale.split('_');
    if (parts.length == 2) {
      await Future.wait([
        setLanguageCode(parts[0]),
        setCountryCode(parts[1]),
      ]);
    }
  }

  /// 获取初始化路由
  static getInitialRoute(){
    bool isFirst = SpUtil.getData(ConfigUtil.isFirst,defaultValue: false)??false;
    if (isFirst) {
      return AppPages.welcome;
    } else {
      return AppPages.main;
    }
  }
  

  /// 从存储加载语言配置
  void _loadLanguage() {
    final languageCode = SpUtil.getData<String>(_languageCodeKey);
    final countryCode = SpUtil.getData<String>(_countryCodeKey);
    
    if (languageCode != null) {
      _languageCode = languageCode;
    }
    if (countryCode != null) {
      _countryCode = countryCode;
    }
  }

  /// 字体大小相关
  double _fontSize = defaultFontSize;
  
  /// 获取当前字体大小
  double get fontSize => _fontSize;

  /// 设置字体大小
  /// [size] 字体大小，单位为逻辑像素
  Future<void> setFontSize(double size) async {
    _fontSize = size;
    await SpUtil.setData(_fontSizeKey, size);
  }

  /// 从存储加载字体大小
  void _loadFontSize() {
    final size = SpUtil.getData<double>(_fontSizeKey);
    if (size != null) {
      _fontSize = size;
    }
  }

  /// 重置所有配置到默认值
  /// 会清除所有已保存的配置
  Future<void> resetToDefault() async {
    await Future.wait([
      setThemeMode(defaultThemeMode),
      setLanguageCode(defaultLanguageCode),
      setCountryCode(defaultCountryCode),
      setFontSize(defaultFontSize),
    ]);
  }

  /// 判断是否为深色模式
  /// 根据当前主题模式和系统设置判断
  /// [context] 用于获取系统主题模式的上下文
  bool isDarkMode(BuildContext context) {
    switch (_themeMode) {
      case ThemeMode.system:
        return MediaQuery.of(context).platformBrightness == Brightness.dark;
      case ThemeMode.dark:
        return true;
      case ThemeMode.light:
        return false;
    }
  }

  static bool isUnauthorized(int code, String msg) {
    if (code == 401) {
      ToastUtil.showToast("登录信息已过期，请重新登录");
      Get.offAllNamed(AppPages.login);
      return false;
    } else if (code != 200) {
      ToastUtil.showToast(msg);
      return false;
    }
    return true;
  }

  // 自定义iconfont图标
  // static iconFont(int codePoint, {double size = 16.0, Color? color}) {
  //   return Icon(
  //     IconData(codePoint, fontFamily: 'iconfont', matchTextDirection: true),
  //     size: size,
  //     color: color,
  //   );
  // }

  /// 获取指定月份的第一天和最后一天
  /// 如果指定月份是当前月份，最后一天为当前日期
  /// [yearMonth] 年月字符串，例如："2025年01月"
  /// 返回格式为 {firstDay: 'yyyy-MM-dd 00:00:00', lastDay: 'yyyy-MM-dd 23:59:59'}
  static Map<String, String> getMonthFirstAndLastDay(String yearMonth) {
    // 解析年月字符串
    // 去除"年"和"月"字符，提取年和月
    final yearStr = yearMonth.substring(0, 4);
    final monthStr = yearMonth.substring(5, 7);
    
    // 转换为整数
    final year = int.parse(yearStr);
    final month = int.parse(monthStr);
    
    // 获取当前日期
    final now = DateTime.now();
    
    // 计算指定月份的第一天
    final firstDay = DateTime(year, month, 1);
    
    // 计算指定月份的最后一天
    DateTime lastDay;
    
    // 如果是当前年月，则最后一天为当前日期
    if (year == now.year && month == now.month) {
      lastDay = now;
    } else {
      // 否则，获取下个月的第一天，然后减去一天
      lastDay = DateTime(year, month + 1, 1).subtract(const Duration(days: 1));
    }
    
    // 格式化日期
    String formatFirstDay = _formatDateTimeStart(firstDay);
    String formatLastDay = _formatDateTimeEnd(lastDay);
    
    return {
      'firstDay': formatFirstDay,
      'lastDay': formatLastDay
    };
  }
  
  /// 格式化日期时间为 'yyyy-MM-dd 00:00:00' 格式（当天开始时间）
  static String _formatDateTimeStart(DateTime dateTime) {
    String year = dateTime.year.toString();
    String month = dateTime.month.toString().padLeft(2, '0');
    String day = dateTime.day.toString().padLeft(2, '0');
    
    return '$year-$month-$day 00:00:00';
  }
  
  /// 格式化日期时间为 'yyyy-MM-dd 23:59:59' 格式（当天结束时间）
  static String _formatDateTimeEnd(DateTime dateTime) {
    String year = dateTime.year.toString();
    String month = dateTime.month.toString().padLeft(2, '0');
    String day = dateTime.day.toString().padLeft(2, '0');
    
    return '$year-$month-$day 23:59:59';
  }
  
  /// 获取今天的开始时间和结束时间
  /// 返回格式为 {startTime: 'yyyy-MM-dd 00:00:00', endTime: 'yyyy-MM-dd 23:59:59'}
  static Map<String, String> getTodayStartAndEnd() {
    final today = DateTime.now();
    
    return {
      'startTime': _formatDateTimeStart(today),
      'endTime': _formatDateTimeEnd(today)
    };
  }
  
  /// 获取昨天的开始时间和结束时间
  /// 返回格式为 {startTime: 'yyyy-MM-dd 00:00:00', endTime: 'yyyy-MM-dd 23:59:59'}
  static Map<String, String> getYesterdayStartAndEnd() {
    final today = DateTime.now();
    final yesterday = today.subtract(const Duration(days: 1));
    
    return {
      'startTime': _formatDateTimeStart(yesterday),
      'endTime': _formatDateTimeEnd(yesterday)
    };
  }
  /// 返回格式为 "yyyy-MM-dd 00:00:00" 的字符串
  static String formatDateTimeWithZeroTime(DateTime dateTime) {
    final DateFormat formatter = DateFormat('yyyy-MM-dd HH:mm:ss');
    return formatter.format(dateTime);
  }
}
