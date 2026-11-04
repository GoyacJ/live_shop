import 'dart:io';
import 'package:dio/dio.dart';
import '../utils/log_utils.dart';

/// 重试拦截器
/// 用于处理请求失败时的自动重试逻辑，支持以下功能：
/// 1. 可配置最大重试次数
/// 2. 可配置重试延迟时间
/// 3. 智能判断是否需要重试（网络错误、超时、服务器错误等）
/// 4. 支持自定义需要重试的HTTP状态码
class RetryInterceptor extends Interceptor {
  /// 最大重试次数
  final int maxRetries;

  /// 重试间隔时间
  /// 实际重试间隔 = retryDelay * (当前重试次数 + 1)
  final Duration retryDelay;

  /// 需要重试的HTTP状态码列表
  /// 默认包含：408(请求超时)、500(服务器错误)、502(网关错误)、503(服务不可用)、504(网关超时)
  final List<int> retryStatusCodes;

  /// 创建重试拦截器实例
  /// [maxRetries] 最大重试次数，默认3次
  /// [retryDelay] 重试基础延迟时间，默认1秒
  /// [retryStatusCodes] 需要重试的HTTP状态码列表
  RetryInterceptor({
    this.maxRetries = 3,
    this.retryDelay = const Duration(seconds: 1),
    this.retryStatusCodes = const [408, 500, 502, 503, 504],
  });

  @override
  Future<void> onError(
      DioException err, ErrorInterceptorHandler handler) async {
    final options = err.requestOptions;

    // 获取当前重试次数
    options.extra['retryCount'] ??= 0;
    final int currentRetryCount = options.extra['retryCount'];

    // 判断是否需要重试
    if (_shouldRetry(err) && currentRetryCount < maxRetries) {
      LogUtils.d(
        'Retrying request======data----{attempt: ${currentRetryCount + 1}, maxRetries: $maxRetries, path: ${options.path}}',
      );

      // 更新重试次数
      options.extra['retryCount'] = currentRetryCount + 1;

      // 延迟重试，每次重试的延迟时间会递增
      // 例如：第一次重试等待1秒，第二次等待2秒，第三次等待3秒
      await Future.delayed(retryDelay * (currentRetryCount + 1));

      try {
        // 重新发起请求
        // 使用新的Dio实例避免重复使用拦截器
        final response = await Dio().fetch(options);
        return handler.resolve(response);
      } on DioException catch (e) {
        // 重试失败，交给下一个错误处理器
        return super.onError(e, handler);
      }
    }

    // 达到最大重试次数或不需要重试，继续向下传递错误
    return super.onError(err, handler);
  }

  /// 判断是否需要重试请求
  /// 以下情况会进行重试：
  /// 1. 网络连接错误（SocketException）
  /// 2. 请求超时（连接超时、接收超时、发送超时）
  /// 3. 服务器返回特定的HTTP状态码（通过retryStatusCodes配置）
  /// 
  /// [err] Dio错误对象
  /// 返回true表示需要重试，false表示不需要重试
  bool _shouldRetry(DioException err) {
    // 网络错误重试
    if (err.error is SocketException) {
      return true;
    }

    // 超时错误重试
    if (err.type == DioExceptionType.connectionTimeout ||
        err.type == DioExceptionType.receiveTimeout ||
        err.type == DioExceptionType.sendTimeout) {
      return true;
    }

    // 服务器错误重试
    if (err.response?.statusCode != null &&
        retryStatusCodes.contains(err.response!.statusCode)) {
      return true;
    }

    return false;
  }
}
