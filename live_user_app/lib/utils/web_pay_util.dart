import 'dart:async';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/web_pay_platform.dart' as platform;

/// H5支付工具类
/// 支持微信H5支付、支付宝H5支付等
///
/// 主要功能：
/// 1. 创建H5支付订单
/// 2. 发起H5支付
/// 3. 支付结果回调处理
/// 4. 支付状态查询
/// 5. 支付失败重试
/// 6. 支付状态轮询
/// 7. 支付环境检查
class WebPayUtil {
  /// 支付状态枚举
  static const String PAY_STATUS_PENDING = 'pending'; // 待支付
  static const String PAY_STATUS_SUCCESS = 'success'; // 支付成功
  static const String PAY_STATUS_FAILED = 'failed'; // 支付失败
  static const String PAY_STATUS_CANCELLED = 'cancelled'; // 支付取消

  /// 支付渠道枚举
  static const String PAY_CHANNEL_WECHAT = 'wx_h5'; // 微信H5支付
  static const String PAY_CHANNEL_ALIPAY = 'alipay_h5'; // 支付宝H5支付

  /// 轮询间隔（秒）
  static const int POLL_INTERVAL = 2;

  /// 最大轮询次数（5分钟）
  static const int MAX_POLL_COUNT = 150; // 5分钟 = 150次 * 2秒

  /// 当前轮询定时器
  static Timer? _pollTimer;

  /// 当前轮询次数
  static int _pollCount = 0;

  /// 当前支付订单ID
  static String? _currentPaymentOrderId;

  /// 当前支付回调函数
  static Function(String orderId)? _currentOnSuccess;
  static Function(String orderId, String error)? _currentOnFailed;
  static Function(String orderId)? _currentOnCancelled;
  static Function(String status, String message)? _currentOnProgress;

  /// 页面可见性监听器
  static Object? _visibilityListener;

  /// 发起 H5 支付
  ///
  /// [orderId] 订单号
  /// [payUrl] 支付地址（必需）
  /// [payChannel] 支付渠道 (wx_h5, alipay_h5)
  /// [onSuccess] 支付成功回调
  /// [onFailed] 支付失败回调
  /// [onCancelled] 支付取消回调
  /// [onProgress] 支付进度回调
  static Future<void> launchH5Pay({
    required String orderId,
    required String payUrl,
    String payChannel = PAY_CHANNEL_WECHAT,
    Function(String orderId)? onSuccess,
    Function(String orderId, String error)? onFailed,
    Function(String orderId)? onCancelled,
    Function(String status, String message)? onProgress,
  }) async {
    try {
      // 显示加载提示
      onProgress?.call('loading', '正在获取支付链接...');
      LogUtils.d('支付URL: $payUrl');

      // 开始轮询支付状态
      _startPaymentPolling(
        orderId: orderId,
        onSuccess: onSuccess,
        onFailed: onFailed,
        onCancelled: onCancelled,
        onProgress: onProgress,
      );

      // 在跳转支付页面之前，先检查一次支付状态
      // 防止用户已经在其他页面完成支付的情况
      try {
        final currentStatus = await _queryPaymentStatus(orderId);
        if (currentStatus == PAY_STATUS_SUCCESS) {
          LogUtils.d('检测到支付已完成，停止轮询并处理成功回调');
          _stopPaymentPolling();
          onProgress?.call('success', '支付成功');
          onSuccess?.call(orderId);
          return; // 不跳转支付页面
        }
      } catch (e) {
        LogUtils.w('预检查支付状态失败，继续正常流程: $e');
      }

      onProgress?.call('redirecting', '正在跳转支付页面...');

      // // 构建重定向URL - 支付成功后跳转到订单列表页面
      // final redirectHash = Uri.encodeComponent('#/OrderListPage');
      // final redirectPath = 'https://live.h5.weilaizhihuigu.cn/redirect.html?redirect_hash=$redirectHash';
      //
      // final encodedRedirectUrl = Uri.encodeComponent(redirectPath);

      // 检查支付URL是否已经包含参数
      // String finalPayUrl = '$payUrl&redirect_url=$encodedRedirectUrl';

      // LogUtils.d('最终支付地址: $finalPayUrl');
      // 跳转到支付页面
      await platform.openPayUrl(payUrl); // 不刷新当前页面

      // html.window.location.href = finalPayUrl;
    } catch (e) {
      final errorMsg = '支付发起失败：${e.toString()}';
      LogUtils.e('支付发起失败', error: e);
      onProgress?.call('error', errorMsg);
      onFailed?.call(orderId, errorMsg);
      ToastUtil.showToast(errorMsg);
    }
  }

  /// 开始支付状态轮询
  static void _startPaymentPolling({
    required String orderId,
    Function(String orderId)? onSuccess,
    Function(String orderId, String error)? onFailed,
    Function(String orderId)? onCancelled,
    Function(String status, String message)? onProgress,
  }) {
    // 先取消之前的轮询（但不清除回调函数）
    _pollTimer?.cancel();
    _pollTimer = null;
    _pollCount = 0;

    // 移除旧的页面可见性监听器
    if (_visibilityListener != null) {
      try {
        platform.removePageVisibilityListener(_visibilityListener!);
        _visibilityListener = null;
      } catch (e) {
        LogUtils.w('移除页面可见性监听器失败: $e');
      }
    }

    // 保存当前支付信息
    _currentPaymentOrderId = orderId;
    _currentOnSuccess = onSuccess;
    _currentOnFailed = onFailed;
    _currentOnCancelled = onCancelled;
    _currentOnProgress = onProgress;

    // 添加页面可见性检测
    _setupPageVisibilityListener();

    // 开始新的轮询
    _pollTimer =
        Timer.periodic(Duration(seconds: POLL_INTERVAL), (timer) async {
      _pollCount++;

      // 先检查是否超过最大轮询次数
      if (_pollCount >= MAX_POLL_COUNT) {
        _stopPaymentPolling();
        onProgress?.call('timeout', '支付超时');
        onFailed?.call(orderId, '支付超时，请重新发起支付');
        ToastUtil.showToast('支付超时，请重新发起支付');
        return;
      }

      try {
        // 查询支付状态
        final status = await _queryPaymentStatus(orderId);

        switch (status) {
          case PAY_STATUS_SUCCESS:
            _stopPaymentPolling();
            onProgress?.call('success', '支付成功');
            onSuccess?.call(orderId);
            break;

          case PAY_STATUS_FAILED:
            _stopPaymentPolling();
            onProgress?.call('failed', '支付失败');
            onFailed?.call(orderId, '支付失败');
            break;

          case PAY_STATUS_CANCELLED:
            _stopPaymentPolling();
            onProgress?.call('cancelled', '支付已取消');
            onCancelled?.call(orderId);
            break;

          case PAY_STATUS_PENDING:
            onProgress?.call('pending', '等待支付中...');
            break;

          default:
            onProgress?.call('unknown', '支付状态未知');
            break;
        }
      } catch (e) {
        LogUtils.e('查询支付状态失败', error: e);
        onProgress?.call('error', '查询支付状态失败');

        // 如果查询失败次数过多，停止轮询
        if (_pollCount >= MAX_POLL_COUNT) {
          _stopPaymentPolling();
          onFailed?.call(orderId, '查询支付状态失败');
        }
      }
    });
  }

  /// 停止支付状态轮询
  static void _stopPaymentPolling() {
    _pollTimer?.cancel();
    _pollTimer = null;
    _pollCount = 0;

    // 移除页面可见性监听器
    if (_visibilityListener != null) {
      try {
        platform.removePageVisibilityListener(_visibilityListener!);
        _visibilityListener = null;
      } catch (e) {
        LogUtils.w('移除页面可见性监听器失败: $e');
      }
    }

    // 清除当前支付信息
    _currentPaymentOrderId = null;
    _currentOnSuccess = null;
    _currentOnFailed = null;
    _currentOnCancelled = null;
    _currentOnProgress = null;
  }

  /// 设置页面可见性检测
  static void _setupPageVisibilityListener() {
    try {
      // 先移除旧的监听器
      if (_visibilityListener != null) {
        platform.removePageVisibilityListener(_visibilityListener!);
      }

      // 创建新的监听器
      _visibilityListener = platform.addPageVisibilityListener(() {
        if (platform.pageIsVisible && _currentPaymentOrderId != null) {
          LogUtils.d('页面重新可见，立即检查支付状态');
          _checkPaymentStatusOnPageVisible();
        }
      });
    } catch (e) {
      LogUtils.w('设置页面可见性检测失败: $e');
    }
  }

  /// 页面可见时检查支付状态
  static Future<void> _checkPaymentStatusOnPageVisible() async {
    if (_currentPaymentOrderId == null) return;

    try {
      final status = await _queryPaymentStatus(_currentPaymentOrderId!);

      switch (status) {
        case PAY_STATUS_SUCCESS:
          LogUtils.d('页面可见时检测到支付成功');
          _stopPaymentPolling();
          _currentOnProgress?.call('success', '支付成功');
          _currentOnSuccess?.call(_currentPaymentOrderId!);
          break;

        case PAY_STATUS_FAILED:
          LogUtils.d('页面可见时检测到支付失败');
          _stopPaymentPolling();
          _currentOnProgress?.call('failed', '支付失败');
          _currentOnFailed?.call(_currentPaymentOrderId!, '支付失败');
          break;

        case PAY_STATUS_CANCELLED:
          LogUtils.d('页面可见时检测到支付取消');
          _stopPaymentPolling();
          _currentOnProgress?.call('cancelled', '支付已取消');
          _currentOnCancelled?.call(_currentPaymentOrderId!);
          break;

        default:
          LogUtils.d('页面可见时支付状态: $status');
          break;
      }
    } catch (e) {
      LogUtils.e('页面可见时检查支付状态失败', error: e);
    }
  }

  /// 查询支付状态
  static Future<String> _queryPaymentStatus(String orderId) async {
    try {
      // 优先使用支付状态查询接口
      try {
        final result = await NetUtil.getPaymentStatus(orderId);

        if (!ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
          final paymentStatus = result['data']['trade_state']?.toString() ?? '';

          // 根据微信支付文档的trade_state状态返回对应状态
          switch (paymentStatus) {
            case 'SUCCESS':
              return PAY_STATUS_SUCCESS;
            case 'USERPAYING':
              return PAY_STATUS_PENDING;
            case 'NOTPAY':
              return PAY_STATUS_PENDING;
            case 'PAYERROR':
              return PAY_STATUS_FAILED;
            case 'REVOKED':
              return PAY_STATUS_CANCELLED;
            case 'CLOSED':
              return PAY_STATUS_CANCELLED;
            case 'REFUND':
              return PAY_STATUS_CANCELLED;
            default:
              // 未知状态，默认为待支付
              LogUtils.d('未知支付状态: $paymentStatus');
              return PAY_STATUS_PENDING;
          }
        } else {
          throw Exception(result['msg'] ?? '查询支付状态失败');
        }
      } catch (e) {
        // 如果支付状态查询接口不存在或失败，回退到订单详情查询
        LogUtils.d('支付状态查询接口不可用，使用订单详情查询: ${e.toString()}');

        final result = await NetUtil.getOrderDetail(orderId);

        if (!ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
          final orderStatus = result['data']['status']?.toString() ?? '0';

          // 根据订单状态返回支付状态
          switch (orderStatus) {
            case '0': // 待支付
              return PAY_STATUS_PENDING;
            case '1': // 已支付
              return PAY_STATUS_SUCCESS;
            case '2': // 已发货
              return PAY_STATUS_SUCCESS;
            case '3': // 已收货
              return PAY_STATUS_SUCCESS;
            case '4': // 已完成
              return PAY_STATUS_SUCCESS;
            case '5': // 已取消
              return PAY_STATUS_CANCELLED;
            default:
              return PAY_STATUS_PENDING;
          }
        } else {
          throw Exception(result['msg'] ?? '查询支付状态失败');
        }
      }
    } catch (e) {
      LogUtils.e('查询支付状态异常', error: e);
      rethrow;
    }
  }

  /// 取消支付
  ///
  /// [orderId] 订单ID
  static Future<void> cancelPayment(String orderId) async {
    try {
      // 停止轮询
      _stopPaymentPolling();

      // 调用取消订单接口
      final result = await NetUtil.cancelOrder(orderId);

      if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
        ToastUtil.showToast('订单已取消');
      } else {
        ToastUtil.showToast(result['msg'] ?? '取消订单失败');
      }
    } catch (e) {
      LogUtils.e('取消支付失败', error: e);
      ToastUtil.showToast('取消支付失败：${e.toString()}');
    }
  }

  /// 重新发起支付
  ///
  /// [orderId] 订单ID
  /// [payChannel] 支付渠道
  /// [onSuccess] 支付成功回调
  /// [onFailed] 支付失败回调
  /// [onCancelled] 支付取消回调
  /// [onProgress] 支付进度回调
  static Future<void> retryPayment({
    required String orderId,
    required String payUrl,
    String payChannel = PAY_CHANNEL_WECHAT,
    Function(String orderId)? onSuccess,
    Function(String orderId, String error)? onFailed,
    Function(String orderId)? onCancelled,
    Function(String status, String message)? onProgress,
  }) async {
    // 先取消之前的支付
    await cancelPayment(orderId);

    // 重新发起支付
    await launchH5Pay(
      orderId: orderId,
      payUrl: payUrl,
      payChannel: payChannel,
      onSuccess: onSuccess,
      onFailed: onFailed,
      onCancelled: onCancelled,
      onProgress: onProgress,
    );
  }

  /// 清理资源
  ///
  /// 在页面销毁时调用，清理定时器等资源
  static void dispose() {
    _stopPaymentPolling();
  }
}
