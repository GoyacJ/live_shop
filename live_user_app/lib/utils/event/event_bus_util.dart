import 'dart:async';
import 'package:event_bus_plus/event_bus_plus.dart';

/// 事件总线工具类
/// 用于应用内的事件发布和订阅
class EventBusUtil {
  static final EventBusUtil _instance = EventBusUtil._internal();
  factory EventBusUtil() => _instance;
  EventBusUtil._internal();

  /// 事件总线实例
  final EventBus _eventBus = EventBus();

  /// 发布事件
  /// [event] 要发布的事件对象
  void emit(dynamic event) {
    _eventBus.fire(event);
  }

  /// 订阅事件
  /// [T] 事件类型
  /// [onData] 事件处理回调
  /// [onError] 错误处理回调
  /// [cancelOnError] 是否在错误时取消订阅
  /// 返回订阅对象，可用于取消订阅
  StreamSubscription<T> on<T extends AppEvent>(
    void Function(T event) onData, {
    Function? onError,
    bool cancelOnError = false,
  }) {
    return _eventBus.on<T>().listen(
      onData,
      onError: onError,
      cancelOnError: cancelOnError,
    );
  }
}

/// 常用事件类型定义
class AppEvents {
  /// 用户登录事件
  static const String userLogin = 'user_login';
  
  /// 用户登出事件
  static const String userLogout = 'user_logout';
  
  /// 网络状态变化事件
  static const String networkChanged = 'network_changed';
  
  /// 主题切换事件
  static const String themeChanged = 'theme_changed';
  
  /// 语言切换事件
  static const String languageChanged = 'language_changed';
  
  /// 数据刷新事件
  static const String dataRefresh = 'data_refresh';
}

/// 基础事件类
abstract class BaseEvent extends AppEvent {
  final String type;
  final DateTime timestamp;
  final Map<String, dynamic>? data;

  BaseEvent(this.type, {this.data}) : timestamp = DateTime.now();

  @override
  List<Object?> get props => [type, timestamp, data];

  @override
  String toString() {
    return 'BaseEvent{type: $type, timestamp: $timestamp, data: $data}';
  }
}

/// 简单事件类
class SimpleEvent extends BaseEvent {
  SimpleEvent(super.type, {super.data});
}

/// 用户事件
class UserEvent extends BaseEvent {
  final String userId;
  final String? username;

  UserEvent(super.type, this.userId, {this.username, super.data});

  @override
  List<Object?> get props => [...super.props, userId, username];
}

/// 网络事件
class NetworkEvent extends BaseEvent {
  final bool isConnected;
  final String? connectionType;

  NetworkEvent(this.isConnected, {this.connectionType})
      : super(AppEvents.networkChanged, data: {
          'isConnected': isConnected,
          'connectionType': connectionType,
        });

  @override
  List<Object?> get props => [...super.props, isConnected, connectionType];
}

/// 主题事件
class ThemeEvent extends BaseEvent {
  final String themeMode; // 'light', 'dark', 'system'

  ThemeEvent(this.themeMode)
      : super(AppEvents.themeChanged, data: {'themeMode': themeMode});

  @override
  List<Object?> get props => [...super.props, themeMode];
}
