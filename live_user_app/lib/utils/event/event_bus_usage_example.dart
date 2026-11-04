import 'dart:async';
import 'event_bus_util.dart';

/// EventBus 使用示例
class EventBusUsageExample {
  
  /// 发布用户登录事件
  static void emitUserLogin(String userId, String username) {
    final event = UserEvent(AppEvents.userLogin, userId, username: username);
    EventBusUtil().emit(event);
  }

  /// 发布用户登出事件
  static void emitUserLogout(String userId) {
    final event = UserEvent(AppEvents.userLogout, userId);
    EventBusUtil().emit(event);
  }

  /// 发布网络状态变化事件
  static void emitNetworkChanged(bool isConnected, {String? connectionType}) {
    final event = NetworkEvent(isConnected, connectionType: connectionType);
    EventBusUtil().emit(event);
  }

  /// 发布主题切换事件
  static void emitThemeChanged(String themeMode) {
    final event = ThemeEvent(themeMode);
    EventBusUtil().emit(event);
  }

  /// 发布数据刷新事件
  static void emitDataRefresh() {
    final event = SimpleEvent(AppEvents.dataRefresh);
    EventBusUtil().emit(event);
  }

  /// 发布自定义事件
  static void emitCustomEvent(String eventType, {Map<String, dynamic>? data}) {
    final event = SimpleEvent(eventType, data: data);
    EventBusUtil().emit(event);
  }
}

/// 在 Widget 中使用 EventBus 的示例
class EventBusWidgetExample {
  
  /// 监听用户事件
  static StreamSubscription<UserEvent>? listenToUserEvents(
    void Function(UserEvent event) onEvent,
  ) {
    return EventBusUtil().on<UserEvent>(onEvent);
  }

  /// 监听数据刷新事件
  static StreamSubscription<SimpleEvent>? listenToDataRefresh(
    void Function(SimpleEvent event) onEvent,
  ) {
    return EventBusUtil().on<SimpleEvent>((event) {
      if (event.type == AppEvents.dataRefresh) {
        onEvent(event);
      }
    });
  }

  /// 监听网络状态变化
  static StreamSubscription<NetworkEvent>? listenToNetworkChanges(
    void Function(NetworkEvent event) onEvent,
  ) {
    return EventBusUtil().on<NetworkEvent>(onEvent);
  }

  /// 监听主题变化
  static StreamSubscription<ThemeEvent>? listenToThemeChanges(
    void Function(ThemeEvent event) onEvent,
  ) {
    return EventBusUtil().on<ThemeEvent>(onEvent);
  }
} 