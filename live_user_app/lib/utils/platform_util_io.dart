import 'dart:convert';
import 'dart:io';
import 'dart:async';
import 'package:crypto/crypto.dart';

import 'package:common_utils/common_utils.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:live_app/utils/sp_util.dart';
import 'package:package_info_plus/package_info_plus.dart';

import 'config_util.dart';

/// 是否是生产环境
const bool inProduction = bool.fromEnvironment("dart.vm.product");

class PlatformUtil {
  static Future<PackageInfo> getAppPackageInfo() {
    return PackageInfo.fromPlatform();
  }

  static Future<String> getAppVersion() async {
    final info = await PackageInfo.fromPlatform();
    return info.version;
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
    if (Platform.isAndroid) {
      return await deviceInfo.androidInfo;
    } else if (Platform.isIOS) {
      return await deviceInfo.iosInfo;
    } else {
      return null;
    }
  }

  static Future<bool> isAndroidLow() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      var info = await deviceInfo.androidInfo;
      double ver = (NumUtil.getNumByValueStr(info.version.release ?? '') ??
          999.9) as double;
      if (ver < 9) {
        return true;
      }
    }
    return false;
  }

  static Future<String> getDeviceId() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    if (Platform.isAndroid) {
      var build = await deviceInfo.androidInfo;

      return md5
          .convert(utf8.encode(build.id + build.name + build.device))
          .toString();
    } else if (Platform.isIOS) {
      var data = await deviceInfo.iosInfo;
      return data.identifierForVendor!;
    } else {
      return '';
    }
  }

  //  生成ClientId
  static generateClientId() async {
    bool isAgree =
        SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
    if (!isAgree) {
      return '';
    }

    final deviceInfo = DeviceInfoPlugin();
    String rawId = '';

    if (Platform.isAndroid) {
      final androidInfo = await deviceInfo.androidInfo;
      rawId =
          '${androidInfo.brand}-${androidInfo.device}-${androidInfo.hardware}-${androidInfo.manufacturer}-${androidInfo.model}-${androidInfo.product}';
    } else if (Platform.isIOS) {
      final iosInfo = await deviceInfo.iosInfo;
      rawId =
          '${iosInfo.name}-${iosInfo.model}-${iosInfo.systemName}-${iosInfo.systemVersion}-${iosInfo.identifierForVendor}';
    }
    // 使用 md5 摘要生成唯一 ID
    final bytes = utf8.encode(rawId);
    final digest = md5.convert(bytes);
    return digest.toString();
  }

  static String getOperatingSystem() {
    return Platform.operatingSystem;
  }
} 