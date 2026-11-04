import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import '../utils/log_utils.dart';
import '../utils/toast_util.dart';

/// 错误处理服务
/// 处理全局错误和异常
class ErrorHandler extends GetxService {
  static ErrorHandler get to => Get.find();
  
  /// 初始化错误处理
  void init() {
    // 处理Flutter框架错误
    FlutterError.onError = (FlutterErrorDetails details) {
      FlutterError.presentError(details);
      _handleError(details.exception, details.stack);
    };
    
    // 处理异步错误
    PlatformDispatcher.instance.onError = (error, stack) {
      _handleError(error, stack);
      return true;
    };
    
    // 处理Zone中的错误
    runZonedGuarded(() {
      // 应用主体在这里运行
    }, (error, stackTrace) {
      _handleError(error, stackTrace);
    });
  }
  
  /// 统一错误处理方法
  void _handleError(dynamic error, StackTrace? stackTrace) {
    // 打印错误日志
    LogUtils.e('错误: $error\n堆栈: $stackTrace');
    
    // 显示错误提示
    if (Get.context != null) {
      // handleNetworkError(error);
      LogUtils.e('发生错误: ${_formatError(error)}');
    }
    
    // TODO: 上报错误到服务器
  }
  
  /// 格式化错误信息
  String _formatError(dynamic error) {
    if (error is Exception) {
      return error.toString().replaceAll('Exception: ', '');
    }
    return error.toString();
  }
  
  /// 处理网络错误
  void handleNetworkError(dynamic error) {
    String message;
    if (error is TimeoutException) {
      message = '请求超时，请检查网络';
    } else if (error is NetworkException) {
      message = '网络异常，请检查网络连接';
    } else {
      message = '网络错误，请稍后重试';
    }
    ToastUtil.showToast(message);
  }
}

/// 网络异常
class NetworkException implements Exception {
  final String message;
  NetworkException(this.message);
  
  @override
  String toString() => message;
}
