import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'dart:async';

import '../../widgets/easy_loading_mixin.dart';
import '../../utils/event/event_bus_util.dart';

/// 基础控制器类
/// 提供页面状态管理、生命周期管理、数据加载和错误处理等基础功能
abstract class BaseController extends GetxController with StateMixin,GetSingleTickerProviderStateMixin,
    GetTickerProviderStateMixin,EasyLoadingMixin{

  /// 页面挂载状态，用于防止内存泄漏
  bool _mounted = true;
  bool get mounted => _mounted;
  
  /// 页面刷新状态
  bool _isRefreshing = false;
  bool get isRefreshing => _isRefreshing;
  
  /// 是否为首次加载数据
  bool _isFirstLoad = true;
  bool get isFirstLoad => _isFirstLoad;
  
  /// 上次加载数据的时间，用于防抖
  DateTime? _lastLoadTime;
  
  /// 最小加载间隔时间，防止频繁请求
  static const _minLoadInterval = Duration(milliseconds: 300);

  /// EventBus 订阅列表
  final List<StreamSubscription> _eventSubscriptions = [];

  /// 是否启用事件监听
  bool get enableEventListeners => true;

  /// 需要监听的事件类型列表
  /// 子类可以重写此方法来指定需要监听的事件
  List<String> get eventTypesToListen => [
    AppEvents.userLogin,
    AppEvents.userLogout,
    AppEvents.dataRefresh,
  ];

  @override
  void onInit() {
    super.onInit();
    debugPrint('${runtimeType.toString()} >>> onInit');
    
    // 注册事件监听
    if (enableEventListeners) {
      _registerEventListeners();
    }
    
    loadData();
  }

  @override
  void onReady() {
    super.onReady();
    debugPrint('${runtimeType.toString()} >>> onReady');
  }

  @override
  void onClose() {
    debugPrint('${runtimeType.toString()} >>> onClose');
    _mounted = false;
    
    // 取消所有事件监听
    _unregisterEventListeners();
    
    super.onClose();
  }
  
  /// 生命周期方法，当页面获得焦点时调用
  void onResume() {}
  
  /// 生命周期方法，当页面失去焦点时调用
  void onPause() {}
  
  /// 生命周期方法，当页面处于非活动状态时调用
  void onInactive() {}
  
  /// 生命周期方法，当页面被销毁时调用
  void onDetached() {}

  /// 生命周期方法，当页面被销毁时调用
  void didChangeMetrics(height) {}

  /// 注册事件监听器
  void _registerEventListeners() {
    final eventBus = EventBusUtil();
    
    // 监听用户事件
    if (eventTypesToListen.contains(AppEvents.userLogin) || 
        eventTypesToListen.contains(AppEvents.userLogout)) {
      final subscription = eventBus.on<UserEvent>((event) {
        if (!mounted) return;
        
        debugPrint('${runtimeType.toString()} >>> 收到用户事件: ${event.type}');
        
        if (event.type == AppEvents.userLogin) {
          onUserLogin(event);
        } else if (event.type == AppEvents.userLogout) {
          onUserLogout(event);
        }
      });
      _eventSubscriptions.add(subscription);
    }

    // 监听数据刷新事件
    if (eventTypesToListen.contains(AppEvents.dataRefresh)) {
      final subscription = eventBus.on<SimpleEvent>((event) {
        if (!mounted) return;
        
        if (event.type == AppEvents.dataRefresh) {
          debugPrint('${runtimeType.toString()} >>> 收到数据刷新事件');
          onDataRefresh(event);
        }
      });
      _eventSubscriptions.add(subscription);
    }

    // 监听网络状态变化事件
    if (eventTypesToListen.contains(AppEvents.networkChanged)) {
      final subscription = eventBus.on<NetworkEvent>((event) {
        if (!mounted) return;
        
        debugPrint('${runtimeType.toString()} >>> 网络状态变化: ${event.isConnected}');
        onNetworkChanged(event);
      });
      _eventSubscriptions.add(subscription);
    }

    // 监听主题变化事件
    if (eventTypesToListen.contains(AppEvents.themeChanged)) {
      final subscription = eventBus.on<ThemeEvent>((event) {
        if (!mounted) return;
        
        debugPrint('${runtimeType.toString()} >>> 主题变化: ${event.themeMode}');
        onThemeChanged(event);
      });
      _eventSubscriptions.add(subscription);
    }
  }

  /// 取消所有事件监听
  void _unregisterEventListeners() {
    for (final subscription in _eventSubscriptions) {
      subscription.cancel();
    }
    _eventSubscriptions.clear();
  }

  /// 用户登录事件处理
  /// 子类可以重写此方法来处理登录事件
  void onUserLogin(UserEvent event) {
    debugPrint('${runtimeType.toString()} >>> 用户登录，刷新数据');
    // 默认行为：刷新数据
    refreshData();
  }

  /// 用户登出事件处理
  /// 子类可以重写此方法来处理登出事件
  void onUserLogout(UserEvent event) {
    debugPrint('${runtimeType.toString()} >>> 用户登出，刷新数据');
    // 默认行为：刷新数据
    refreshData();
  }

  /// 数据刷新事件处理
  /// 子类可以重写此方法来处理数据刷新事件
  void onDataRefresh(SimpleEvent event) {
    debugPrint('${runtimeType.toString()} >>> 数据刷新事件');
    // 默认行为：刷新数据
    refreshData();
  }

  /// 网络状态变化事件处理
  /// 子类可以重写此方法来处理网络状态变化
  void onNetworkChanged(NetworkEvent event) {
    debugPrint('${runtimeType.toString()} >>> 网络状态变化: ${event.isConnected}');
    // 默认行为：网络恢复时刷新数据
    if (event.isConnected) {
      refreshData();
    }
  }

  /// 主题变化事件处理
  /// 子类可以重写此方法来处理主题变化
  void onThemeChanged(ThemeEvent event) {
    debugPrint('${runtimeType.toString()} >>> 主题变化: ${event.themeMode}');
    // 默认行为：不刷新数据，只更新UI
    update();
  }

  /// 加载数据的主要方法
  /// 
  /// 包含以下功能：
  /// 1. 防抖检查，避免频繁加载
  /// 2. 自动管理加载状态
  /// 3. 错误处理
  /// 4. 生命周期检查
  Future<void> loadData() async {
    if (!_shouldLoad()) return;
    
    try {
      showLoading();
      await initData();
      if (mounted) {
        showSuccess();
      }
    } catch (e) {
      if (mounted) {
        handleError(e);
      }
    } finally {
      _isFirstLoad = false;
      _updateLastLoadTime();
    }
  }

  /// 子类需要重写此方法来实现具体的数据加载逻辑
  /// 
  /// 示例：
  /// ```dart
  /// @override
  /// Future<void> initData() async {
  ///   final response = await api.getData();
  ///   data.value = response;
  /// }
  /// ```
  initData() async {}

  /// 刷新数据
  /// 
  /// 与 [loadData] 类似，但专门用于刷新场景：
  /// 1. 不检查首次加载标记
  /// 2. 自动管理刷新状态
  /// 3. 包含错误处理
  Future<void> refreshData() async {

    await Future.delayed(const Duration(seconds: 1));
    if (!_shouldLoad()) return;
    
    try {
      _isRefreshing = true;
      await initData();
      if (mounted) {
        showSuccess();
      }
    } catch (e) {
      if (mounted) {
        handleError(e);
      }
    } finally {
      _isRefreshing = false;
      _updateLastLoadTime();
    }
  }

  /// 检查是否应该加载数据
  /// 
  /// 防抖逻辑：
  /// 1. 首次加载直接返回 true
  /// 2. 检查距离上次加载的时间间隔
  /// 3. 如果间隔小于最小间隔，返回 false
  bool _shouldLoad() {
    if (_lastLoadTime == null) return true;
    
    final now = DateTime.now();
    if (now.difference(_lastLoadTime!) < _minLoadInterval) {
      return false;
    }
    
    return true;
  }
  
  /// 更新最后加载时间
  void _updateLastLoadTime() {
    _lastLoadTime = DateTime.now();
  }

  /// showSuccess
  /// showEmpty
  /// showError
  /// showLoading
  /// 统一处理页面状态
  void showSuccess() {
    ///change getx 页面监听方法提供的方法
    change(null, status: RxStatus.success());
  }

  void showEmpty() {
    change(null, status: RxStatus.empty());
  }

  void showError({String? errorMessage, Exception? e}) {
    if (e != null) {
      if (e is DioException) {
        change(null, status: RxStatus.error(e.message));
      } else {
        change(null, status: RxStatus.error(errorMessage));
      }
    } else {
      change(null, status: RxStatus.error(errorMessage));
    }
  }

  void showLoading() {
    change(null, status: RxStatus.loading());
  }

  /// 统一的错误处理方法
  ///
  /// 将错误对象转换为字符串并显示错误状态
  void handleError(dynamic error) {
    showError(errorMessage: error.toString());
  }
}
