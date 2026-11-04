import 'dart:convert';
import 'dart:async';
import 'package:crypto/crypto.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:live_app/utils/sp_util.dart';
import 'package:package_info_plus/package_info_plus.dart';

import 'config_util.dart';

/// 是否是生产环境
const bool inProduction = const bool.fromEnvironment("dart.vm.product");

class PlatformUtil {
  static Future<PackageInfo> getAppPackageInfo() {
    return PackageInfo.fromPlatform();
  }

  static Future<String> getAppVersion() async {
    // Web 平台手动返回版本号（可配置）
    return '1.0.0-web'; // 或从环境读取
  }

  static Future<String> getBuildNum() async {
    PackageInfo packageInfo = await PackageInfo.fromPlatform();
    return packageInfo.buildNumber;
  }

  static Future getDeviceInfo() async {
    // 检查是否同意隐私政策
    final isAgreement =
        SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
    if (!isAgreement) {
      return null;
    }
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    return await deviceInfo.webBrowserInfo;
  }

  static Future<bool> isAndroidLow() async {
    // Web平台不支持Android版本检查
    return false;
  }

  static Future<String> getDeviceId() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    var webInfo = await deviceInfo.webBrowserInfo;
    // 组合设备识别信息（可根据需求增加/删减字段）
    final raw = '${webInfo.userAgent ?? ''}'
        '${webInfo.platform ?? ''}'
        '${webInfo.vendor ?? ''}'
        '${webInfo.hardwareConcurrency ?? ''}';

    final deviceId = md5.convert(utf8.encode(raw)).toString();
    return deviceId;
  }

  //  生成ClientId
  static generateClientId() async {
    bool isAgree =
        SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
    if (!isAgree) {
      return '';
    }

    final deviceInfo = DeviceInfoPlugin();
    final webInfo = await deviceInfo.webBrowserInfo;
    
    // Web平台使用浏览器信息生成ClientId
    String rawId = '${webInfo.userAgent ?? ''}'
        '${webInfo.platform ?? ''}'
        '${webInfo.vendor ?? ''}'
        '${webInfo.hardwareConcurrency ?? ''}'
        '${webInfo.appName ?? ''}'
        '${webInfo.appVersion ?? ''}';
    
    // 使用 md5 摘要生成唯一 ID
    final bytes = utf8.encode(rawId);
    final digest = md5.convert(bytes);
    return digest.toString();
  }

  static String getOperatingSystem() {
    return 'web';
  }
} 