import 'dart:io';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionUtil {
  // Android 所需权限，区分 Android 13+
  static List<Permission> get androidPermissions {
    if (Platform.isAndroid && (Platform.version.contains("33") || Platform.version.contains("13"))) {
      return <Permission>[
        Permission.photos,
        Permission.videos,
        Permission.audio,
      ];
    } else {
      return <Permission>[
        Permission.storage,
      ];
    }
  }

  // iOS 所需权限（可根据业务扩展）
  static List<Permission> get iosPermissions => <Permission>[
    Permission.photos,
  ];

  /// 请求全部权限
  static Future<Map<Permission, PermissionStatus>> requestAll() async {
    final permissions = Platform.isIOS ? iosPermissions : androidPermissions;
    return await permissions.request();
  }

  /// 单独请求一个权限
  static Future<PermissionStatus> request(Permission permission) async {
    // 如果已授权，直接返回
    if (await permission.isGranted) return PermissionStatus.granted;

    // 请求权限
    final result = await permission.request();
    if (result.isDenied || result.isPermanentlyDenied) {
      _showPermissionDeniedToast(permission);
    }
    return result;
  }

  /// 检查权限是否已授权
  static Future<bool> checkGranted(Permission permission) async {
    return await permission.isGranted;
  }

  /// 是否有权限被拒绝
  static bool isDenied(Map<Permission, PermissionStatus> result) {
    return result.values.any((status) =>
    status == PermissionStatus.denied ||
        status == PermissionStatus.permanentlyDenied
    );
  }

  /// 打开系统设置
  static Future<bool> openAppSettingsIfNeeded(Permission permission) async {
    if (await permission.isPermanentlyDenied) {
      return await openAppSettings();
    }
    return false;
  }

  /// 拒绝提示
  static void _showPermissionDeniedToast(Permission permission) {
    Fluttertoast.showToast(
      msg: "请前往系统设置开启权限：${_getPermissionName(permission)}",
      toastLength: Toast.LENGTH_LONG,
      gravity: ToastGravity.BOTTOM,
    );
  }

  static String _getPermissionName(Permission permission) {
    switch (permission) {
      case Permission.storage:
        return "存储";
      case Permission.photos:
        return "照片";
      case Permission.videos:
        return "视频";
      case Permission.audio:
        return "音频";
      default:
        return "相关";
    }
  }
}
