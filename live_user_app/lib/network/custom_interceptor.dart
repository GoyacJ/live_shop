// 请求拦截器
import 'dart:convert';
import 'package:common_utils/common_utils.dart';
import 'package:crypto/crypto.dart';
import 'package:dio/dio.dart';
import 'package:live_app/utils/config_util.dart';

import '../init/config.dart';
import '../utils/log_utils.dart';
import '../utils/platform_util.dart';
import '../utils/sp_util.dart';
import '../utils/toast_util.dart';

class CustomInterceptor extends Interceptor {
  CustomInterceptor();

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    // options.headers['content-type'] = 'application/json';
    // 检查是否同意隐私政策
    final isAgreement = SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
    if (isAgreement) {
      options.headers['app-version'] = await PlatformUtil.getAppVersion();
      // 使用PlatformUtil.getOperatingSystem()替代直接使用Platform.operatingSystem
      options.headers['device-type'] = PlatformUtil.getOperatingSystem();
      options.headers['device-id'] = await PlatformUtil.getDeviceId();
    } else {
      options.headers['app-version'] = '';
      options.headers['device-type'] = '';
      options.headers['device-id'] = '';
    }
    options.headers['timestamp'] = DateTime.now().millisecondsSinceEpoch.toString();
    String nonce = DateTime.now().millisecondsSinceEpoch.toString();
    options.headers['nonce'] = nonce;

    Map<String, dynamic> params = {};
    if (options.data is Map) {
      options.headers['content-type'] = 'application/json';
      params = Map<String, dynamic>.from(options.data);
    }
    if (options.queryParameters.isNotEmpty) {
      params.addAll(options.queryParameters);
    }
    LogUtils.d("请求参数：${options.data}");
    LogUtils.d("get请求参数：${options.queryParameters}");


    params['app-version'] = options.headers['app-version'];
    params['device-type'] = options.headers['device-type'];
    params['device-id'] = options.headers['device-id'];
    params['timestamp'] = options.headers['timestamp'];
    params['nonce'] = options.headers['nonce'];

    String? token = SpUtil.getData<String>(ConfigUtil.token);
    if (ObjectUtil.isNotEmpty(token)) {
      LogUtils.d("DEBUG-token:$token");
      options.headers['authorization'] = token;
      params['authorization'] = options.headers['authorization'];
    }

    List<String> sortedKeys = params.keys.toList()..sort();
    String paramString =
    sortedKeys.map((key) => '$key=${params[key]}').join("&");

    LogUtils.d("DEBUG-签名字符串：$paramString");

    String dataToSign = paramString + Config.apiHost.apiSecret;
    String signature = md5.convert(utf8.encode(dataToSign)).toString();
    options.headers['signature'] = signature;
    LogUtils.d("headers: ${options.headers}");
    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    LogUtils.d('DEBUG-Response: ${jsonEncode(response.data)}');
    LogUtils.d('DEBUG-Path: ${response.requestOptions.path}');

    try {
      if (response.data is Map<String, dynamic>) {
        final Map<String, dynamic> responseData = response.data;
        final int code = responseData['code'] as int;
        final String message = responseData['msg'] as String;

        if ([200, 401].contains(code)) {
          handler.next(response);
        } else if (code == 500) {
          ToastUtil.showToast(message.isEmpty ? '请求错误' : message,);
          handler.next(response);
        } else {
          throw DioException(
            requestOptions: response.requestOptions,
            response: response,
            type: DioExceptionType.badResponse,
            error: message,
          );
        }
      } else {
        throw DioException(
          requestOptions: response.requestOptions,
          response: response,
          type: DioExceptionType.badResponse,
          error: 'Invalid response format',
        );
      }
    } catch (e) {
      LogUtils.e("错误在这");
      handler.reject(
        DioException(
          requestOptions: response.requestOptions,
          response: response,
          type: DioExceptionType.badResponse,
          error: e.toString(),
        ),
      );
    }
  }
}