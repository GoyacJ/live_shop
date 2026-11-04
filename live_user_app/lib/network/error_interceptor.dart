import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../utils/color_util.dart';
import '../utils/log_utils.dart';

class ErrorInterceptor extends Interceptor {
  @override
  void onError(DioException err, ErrorInterceptorHandler handler) {
    LogUtils.e("网络请求错误", error: err.error, stackTrace: err.stackTrace);
    
    // 统一错误处理
    switch (err.type) {
      case DioExceptionType.connectionTimeout:
      case DioExceptionType.sendTimeout:
      case DioExceptionType.receiveTimeout:
        _showError('网络连接超时');
        break;
      case DioExceptionType.badResponse:
        _handleResponseError(err.response?.statusCode);
        break;
      case DioExceptionType.cancel:
        _showError('请求被取消');
        break;
      default:
        _showError('网络请求错误');
        break;
    }
    super.onError(err, handler);
  }
  
  void _handleResponseError(int? statusCode) {
    switch (statusCode) {
      case 400:
        _showError('请求参数错误');
        break;
      case 401:
        _showError('未授权');
        // TODO: 处理token过期
        break;
      case 403:
        _showError('服务器拒绝执行');
        break;
      case 404:
        _showError('请求服务不存在');
        break;
      case 500:
        _showError('服务器内部错误');
        break;
      case 502:
        _showError('网关错误');
        break;
      case 503:
        _showError('服务不可用');
        break;
      case 505:
        _showError('不支持HTTP协议请求');
        break;
      default:
        _showError('网络请求错误，错误码：$statusCode');
        break;
    }
  }
  
  void _showError(String message) {
    Get.snackbar(
      '错误',
      message,
      snackPosition: SnackPosition.TOP,
      backgroundColor: ColorUtil.white,
      colorText: ColorUtil.black,
      borderRadius: 8,
      margin: EdgeInsets.all(10),
      icon: Icon(Icons.error, color: ColorUtil.white),
      duration: Duration(seconds: 2),
    );
  }
}
