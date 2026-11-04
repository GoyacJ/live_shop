import 'package:dio/dio.dart';
import '../init/config.dart';
import '../network/custom_interceptor.dart';
import '../network/error_interceptor.dart';
import '../network/retry_interceptor.dart';

/// HTTP请求工具类
/// 基于Dio封装，支持请求重试、缓存等功能
/// 
/// 主要功能：
/// 1. 统一的请求配置（超时、headers等）
/// 2. 请求重试机制
/// 3. 错误统一处理
/// 4. 请求日志记录
/// 5. 支持请求缓存
/// 6. 支持取消请求
/// 
/// 使用示例:
/// ```dart
/// // GET请求
/// final result = await HttpUtil.instance.get('/user/info');
/// 
/// // POST请求带参数
/// final data = {'name': 'test'};
/// final result = await HttpUtil.instance.post('/user/create', data: data);
/// 
/// // 文件上传
/// final formData = FormData.fromMap({
///   'file': await MultipartFile.fromFile('./text.txt'),
/// });
/// final result = await HttpUtil.instance.post('/upload', data: formData);
/// ```
class HttpUtil {
  /// 工厂构造函数
  /// 
  /// 返回HttpUtil单例
  factory HttpUtil() => _getInstance();

  /// 获取HttpUtil单例
  /// 
  /// 如果实例不存在，则创建新实例
  static HttpUtil get instance => _getInstance();
  static HttpUtil? _instance;

  /// 获取HttpUtil实例
  /// 
  /// 使用双重检查锁定模式实现单例
  static HttpUtil _getInstance() {
    _instance ??= HttpUtil.init();
    return _instance!;
  }

  /// Dio实例
  late Dio _dio;

  /// 初始化HttpUtil
  /// 
  /// [baseUrl] 基础URL
  /// [headers] 请求头
  /// [timeout] 超时时间
  /// [maxRetries] 重试次数
  /// [retryDelay] 重试间隔
  /// [cacheMaxAge] 缓存最大时间
  /// [forceCache] 是否启用缓存
  HttpUtil.init({
    Map<String, dynamic>? headers,
    Duration timeout = const Duration(seconds: 20),
    int maxRetries = 3,
    Duration retryDelay = const Duration(seconds: 1),
    Duration cacheMaxAge = const Duration(hours: 1),
    bool forceCache = false,
  }) {
    // 基础配置
    final options = BaseOptions(
      baseUrl: Config.apiHost.baseUrl,
      connectTimeout: timeout,
      receiveTimeout: timeout,
      sendTimeout: timeout,
    );

    _dio = Dio(options);

    // 添加拦截器
    _dio.interceptors.addAll([
      CustomInterceptor(),
      // 重试拦截器
      RetryInterceptor(
        maxRetries: maxRetries,
        retryDelay: retryDelay,
      ),
      // 错误处理拦截器
      ErrorInterceptor(),
    ]);
  }

  /// GET请求
  /// 
  /// [path] 请求路径
  /// [queryParameters] URL参数
  Future<Response<T>> get<T>(
    String path, {
    Map<String, dynamic>? queryParameters,
  }) async {
    try {
      final response = await _dio.get<T>(
        path,
        queryParameters: queryParameters,
      );
      return response;
    } on DioException {
      rethrow;
    }
  }

  /// POST请求
  /// 
  /// [path] 请求路径
  /// [data] 请求数据
  Future<Response<T>> post<T>(
    String path, {
    dynamic data,
  }) async {
    try {
      final response = await _dio.post<T>(
        path,
        data: data,
      );
      return response;
    } on DioException {
      rethrow;
    }
  }

  /// PUT请求
  /// 
  /// [path] 请求路径
  /// [data] 请求数据
  Future<Response<T>> put<T>(
    String path, {
    dynamic data,
  }) async {
    try {
      final response = await _dio.put<T>(
        path,
        data: data,
      );
      return response;
    } on DioException {
      rethrow;
    }
  }

  /// DELETE请求
  /// 
  /// [path] 请求路径
  /// [data] 请求数据
  /// [queryParameters] URL参数
  /// [options] 请求选项
  /// [cancelToken] 取消令牌
  Future<Response<T>> delete<T>(
    String path, {
    dynamic data,
    Map<String, dynamic>? queryParameters,
  }) async {
    try {
      final response = await _dio.delete<T>(
        path,
        data: data,
        queryParameters: queryParameters,
      );
      return response;
    } on DioException {
      rethrow;
    }
  }

  /// 下载文件
  /// 
  /// [urlPath] 文件URL
  /// [savePath] 保存路径
  /// [onReceiveProgress] 下载进度回调
  /// [queryParameters] URL参数
  /// [cancelToken] 取消令牌
  /// [deleteOnError] 下载失败是否删除文件
  /// [lengthHeader] 文件长度头
  /// [data] 请求数据
  /// [options] 请求选项
  Future<Response> download(
    String urlPath,
    String savePath, {
    ProgressCallback? onReceiveProgress,
    Map<String, dynamic>? queryParameters,
    CancelToken? cancelToken,
    bool deleteOnError = true,
    String lengthHeader = Headers.contentLengthHeader,
    dynamic data,
    Options? options,
  }) async {
    try {
      final response = await _dio.download(
        urlPath,
        savePath,
        onReceiveProgress: onReceiveProgress,
        queryParameters: queryParameters,
        cancelToken: cancelToken,
        deleteOnError: deleteOnError,
        lengthHeader: lengthHeader,
        data: data,
        options: options,
      );
      return response;
    } on DioException {
      rethrow;
    }
  }
}
