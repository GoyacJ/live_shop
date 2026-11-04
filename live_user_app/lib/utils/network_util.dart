import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';
// import 'toast_util.dart';

/// 网络状态管理服务
/// 监听和管理网络连接状态
class NetworkUtil {
  factory NetworkUtil() => _getInstance();

  static NetworkUtil get instance => _getInstance();
  static NetworkUtil? _instance;

  static NetworkUtil _getInstance() {
    _instance ??= NetworkUtil._initNetwork();
    return _instance!;
  }

  final _isConnected = true.obs;
  final _connectivityResults = <ConnectivityResult>[].obs;

  /// 是否有网络连接
  bool get isConnected => _isConnected.value;

  /// 当前网络连接类型列表
  List<ConnectivityResult> get connectivityResults => _connectivityResults;

  /// 初始化网络监听
  NetworkUtil._initNetwork() {
    // 获取初始网络状态
    Connectivity().checkConnectivity().then((results) {
      _connectivityResults.value = results;
      _updateConnectionStatus(results);
    });

    // 监听网络状态变化
    Connectivity().onConnectivityChanged.listen((results) {
      _connectivityResults.value = results;
      _updateConnectionStatus(results);
    });
  }

  /// 更新连接状态
  void _updateConnectionStatus(List<ConnectivityResult> results) {
    final wasConnected = _isConnected.value;
    _isConnected.value =
        results.any((result) => result != ConnectivityResult.none);

    // 网络状态变化时显示提示
    if (wasConnected != _isConnected.value) {
      if (_isConnected.value) {
        ToastUtil.showToast('网络已连接: ${_getNetworkTypeString()}');
      } else {
        ToastUtil.showToast('网络已断开');
      }
    }
  }

  /// 获取网络类型描述
  String _getNetworkTypeString() {
    final types = _connectivityResults
        .map((result) {
          switch (result) {
            case ConnectivityResult.mobile:
              return '移动网络';
            case ConnectivityResult.wifi:
              return 'WiFi';
            case ConnectivityResult.ethernet:
              return '以太网';
            case ConnectivityResult.vpn:
              return 'VPN';
            case ConnectivityResult.bluetooth:
              return '蓝牙';
            case ConnectivityResult.other:
              return '其他';
            case ConnectivityResult.none:
              return '无网络';
          }
        })
        .where((type) => type != '无网络')
        .toList();

    return types.isEmpty ? '无网络' : types.join(', ');
  }

  /// 检查是否有网络连接
  /// [showToast] 是否显示提示
  bool checkConnection({bool showToast = true}) {
    if (!_isConnected.value && showToast) {
      ToastUtil.showToast('无网络连接');
    }
    return _isConnected.value;
  }

  /// 获取当前活跃的网络类型
  List<String> getActiveNetworkTypes() {
    return _connectivityResults
        .where((result) => result != ConnectivityResult.none)
        .map((result) {
      switch (result) {
        case ConnectivityResult.mobile:
          return '移动网络';
        case ConnectivityResult.wifi:
          return 'WiFi';
        case ConnectivityResult.ethernet:
          return '以太网';
        case ConnectivityResult.vpn:
          return 'VPN';
        case ConnectivityResult.bluetooth:
          return '蓝牙';
        case ConnectivityResult.other:
          return '其他';
        case ConnectivityResult.none:
          return '无网络';
      }
    }).toList();
  }

  /// 是否有指定类型的网络连接
  bool hasNetworkType(ConnectivityResult type) {
    return _connectivityResults.contains(type);
  }

  /// 是否有移动网络连接
  bool get hasMobileNetwork => hasNetworkType(ConnectivityResult.mobile);

  /// 是否有WiFi连接
  bool get hasWifi => hasNetworkType(ConnectivityResult.wifi);

  /// 是否有以太网连接
  bool get hasEthernet => hasNetworkType(ConnectivityResult.ethernet);

  /// 是否有VPN连接
  bool get hasVpn => hasNetworkType(ConnectivityResult.vpn);
}
